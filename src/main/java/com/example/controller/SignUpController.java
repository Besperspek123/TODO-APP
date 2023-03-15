package com.example.controller;

import com.example.exceptions.EmptyCredentialsException;
import com.example.exceptions.InvalidCharactersException;
import com.example.exceptions.LoginOrPasswordTooLongException;
import com.example.hibernate.HibernateSessionFactory;
import com.example.entitiesDatabase.User;
import com.example.controller.constantsNotification.ErrorConstants;
import com.example.controller.constantsNotification.SuccessfulConstants;
import com.example.javafxFxmlLoader.JavaFx;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import org.hibernate.Session;

import javax.persistence.Query;
import java.util.List;

public class SignUpController {

    @FXML
    private Button backToLoginPageButton;

    @FXML
    private PasswordField loginPassword;

    @FXML
    private TextField loginUsername;

    @FXML
    private Button registerButton;

    @FXML
    private TextField name;

    @FXML
    void initialize() {
        setupBackToLoginPageButton();
        setupRegisterButton();
        handleEnterPressKey();
}


    private void setupBackToLoginPageButton (){
        backToLoginPageButton.setOnAction(actionEvent -> {

            backToLoginPageButton.getScene().getWindow().hide();
            JavaFx.SceneSwitcher("/Login.fxml");

        });
    }
    private void setupRegisterButton(){
        registerButton.setOnAction(actionEvent -> {

            try {
                if(loginUsername.getText().length() > 25 || loginPassword.getText().length() > 20){
                    throw new LoginOrPasswordTooLongException("User are trying to enter " +
                            "a username or password that is too long. " +
                            "The login must not be longer than 25 characters, " +
                            "and the password must not be longer than 25 characters ");
                }
                if (loginUsername.getText().isEmpty() || loginPassword.getText().isEmpty()){
                    throw new EmptyCredentialsException("The user did not enter a username or password");
                }

                //Regex checking russian characters in input field login and password
                if (loginUsername.getText().matches(".*[а-яА-Я].*") || loginPassword.getText().matches(".*[а-яА-Я].*")){
                    throw new InvalidCharactersException("The user is trying to enter invalid characters");
                }

                if (!checkUserCredentials()){
                    registerUser();
                    registerButton.getScene().getWindow().hide();
                    JavaFx.SceneSwitcher("/Login.fxml");
                    JavaFx.showInputSuccessfulNotification(SuccessfulConstants.SUCCESSFUL_SIGN_UP);
                }
                else {
                    JavaFx.showInputErrorNotification(ErrorConstants.ERROR_SIGN_UP_ACCOUNT_ALREADY_EXIST);
                }
            }
            catch (EmptyCredentialsException e){
                System.out.println(e.getMessage());
                JavaFx.showInputErrorNotification(ErrorConstants.ERROR_LOGIN_OR_PASSWORD_EMPTY);
            }
            catch (InvalidCharactersException e){
                System.out.println(e.getMessage());
                JavaFx.showInputErrorNotification(ErrorConstants.ERROR_SIGN_UP_INVALID_CHARACTERS);
            }
            catch (LoginOrPasswordTooLongException e){
                System.out.println(e.getMessage());
                JavaFx.showInputErrorNotification(ErrorConstants.ERROR_SIGN_UP_TO_LOONG_CHARACTERS);
            }
        });


    }

    private boolean checkUserCredentials(){
        Session sessionIsUserExist = null;
        List<User> usersForQuery;
        try {
            sessionIsUserExist = HibernateSessionFactory.getCurrentSessionUser();
            sessionIsUserExist.beginTransaction();
            System.out.println("Session check user in database is open");
            String sqlQuery = "from User where loginUsername =:login";
            Query checkUserQuery = sessionIsUserExist.createQuery(sqlQuery);
            checkUserQuery.setParameter("login", loginUsername.getText());

           usersForQuery = checkUserQuery.getResultList();
        }
        finally {
            if(sessionIsUserExist != null && sessionIsUserExist.isOpen()){
                sessionIsUserExist.close();
                System.out.println("Session check user in database is closed");
            }
        }
        return usersForQuery.isEmpty() ? false :true;
    }

    private void registerUser(){
        Session sessionSaveUser = null;
        try {
            sessionSaveUser = HibernateSessionFactory.getCurrentSessionUser();
            sessionSaveUser.beginTransaction();
            System.out.println("Session register user in database is open");
            User user = new User(loginUsername.getText(), loginPassword.getText());
            sessionSaveUser.save(user);
            sessionSaveUser.getTransaction().commit();
        }
        finally {
            if(sessionSaveUser != null && sessionSaveUser.isOpen()){
                sessionSaveUser.close();
                System.out.println("Session register user in database is closed");
            }
        }

    }

    private void handleEnterPressKey() {
        loginPassword.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode()== KeyCode.ENTER){
                registerButton.fire();
            }
        });
        loginUsername.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode()== KeyCode.ENTER){
                registerButton.fire();
            }
        });
        name.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode()== KeyCode.ENTER){
                registerButton.fire();
            }
        });
    }

}
