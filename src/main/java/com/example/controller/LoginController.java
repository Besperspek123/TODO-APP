package com.example.controller;

import com.example.Exceptions.EmptyCredentialsException;
import com.example.Exceptions.UserNotFoundException;
import com.example.Hibernate.HibernateSessionFactory;
import com.example.ObjectsDataBase.User;
import com.example.controller.constantsNotification.ErrorConstants;
import com.example.controller.constantsNotification.SuccessfulConstants;
import com.example.javafxFxmlLoader.SceneSwitcher;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import org.hibernate.Session;

import javax.persistence.Query;
import java.util.List;

public class LoginController {

    private static User currentLoginUser;

    public static String getCurrentUserLogin() {
        return currentLoginUser.getLoginUsername();
    }

    @FXML
    private Button LoginButton;

    @FXML
    private PasswordField LoginPassword;

    @FXML
    private TextField LoginUsername;

    @FXML
    private Button SignUpButton;

    @FXML
    private Button clearDataAndUsersButton;


    @FXML
    void initialize() {



        setupLoginButton();
        setupSignUpButton();
        setupClearUsersAndTasksButton();


    }

    private void setupClearUsersAndTasksButton() {
        clearDataAndUsersButton.setOnAction(actionEvent -> {


            Session sessionDeleteAllUsers = null;

            try {
                sessionDeleteAllUsers = HibernateSessionFactory.getCurrentSessionUser();
                sessionDeleteAllUsers.beginTransaction();
                sessionDeleteAllUsers.createQuery("delete from User").executeUpdate();
            } finally {
                if (sessionDeleteAllUsers != null && sessionDeleteAllUsers.isOpen()) {
                    sessionDeleteAllUsers.close();
                }
            }

            Session sessionDeleteAllTaskFromAllUser = null;
            Session sessionDeleteAllCompletedTaskFromAllUser = null;

            try {
                sessionDeleteAllTaskFromAllUser = HibernateSessionFactory.getCurrentSessionCurrentTask();
                sessionDeleteAllTaskFromAllUser.beginTransaction();
                sessionDeleteAllTaskFromAllUser.createQuery("delete from CurrentTask").executeUpdate();
                sessionDeleteAllTaskFromAllUser.getTransaction().commit();

                sessionDeleteAllCompletedTaskFromAllUser = HibernateSessionFactory.getCurrentSessionCompletedTask();
                sessionDeleteAllCompletedTaskFromAllUser.beginTransaction();
                sessionDeleteAllCompletedTaskFromAllUser.createQuery("delete from CompletedTask").executeUpdate();
                sessionDeleteAllCompletedTaskFromAllUser.getTransaction().commit();

                System.out.println("All users have been deleted");
                SceneSwitcher.showInputSuccessfulNotification(SuccessfulConstants.SUCCESSFUL_DELETE_ALL_USER);

            } finally {
                if (sessionDeleteAllTaskFromAllUser != null && sessionDeleteAllUsers.isOpen()) {
                    sessionDeleteAllTaskFromAllUser.close();
                }if (sessionDeleteAllCompletedTaskFromAllUser != null && sessionDeleteAllCompletedTaskFromAllUser.isOpen()) {
                    sessionDeleteAllCompletedTaskFromAllUser.close();
                }
            }

        });
    }

    private void setupSignUpButton() {
        SignUpButton.setOnAction(actionEvent -> {

            SignUpButton.getScene().getWindow().hide();
            SceneSwitcher.SceneSwitcher("/SignUp.fxml");

        });

    }

    private void setupLoginButton() {
        LoginButton.setOnAction(actionEvent -> {


            Session sessionLoginUser = null;

            try {

                if (LoginUsername.getText().isEmpty() || LoginPassword.getText().isEmpty()){
                    throw new EmptyCredentialsException("The user did not enter a username or password");
                }

                sessionLoginUser = HibernateSessionFactory.getCurrentSessionUser();
                sessionLoginUser.beginTransaction();
                System.out.println("Session login user is open");

                String sqlQuery = "from User where loginUsername =:login and loginPassword =:password";
                Query checkUserQuery = sessionLoginUser.createQuery(sqlQuery);
                checkUserQuery.setParameter("login", LoginUsername.getText());
                checkUserQuery.setParameter("password", LoginPassword.getText());

                List<User> usersForQueryList = checkUserQuery.getResultList();

                if (!usersForQueryList.isEmpty()) {

                    currentLoginUser = usersForQueryList.get(0);

                    LoginButton.getScene().getWindow().hide();
                    SceneSwitcher.SceneSwitcher("/TaskList.fxml");

                } else {
                    throw new UserNotFoundException("User not found");
                }
            } catch (UserNotFoundException e) {
                System.out.println(e.getMessage());
                SceneSwitcher.showInputErrorNotification(ErrorConstants.ERROR_LOGIN_OR_PASSWORD_INCORRECT);

            }
            catch (EmptyCredentialsException e){
                System.out.println(e.getMessage());
                SceneSwitcher.showInputErrorNotification(ErrorConstants.ERROR_LOGIN_OR_SIGNUP_EMPTY_LOGIN_OR_PASSWORD);
            }finally {
                if (sessionLoginUser != null && sessionLoginUser.isOpen()) {
                    sessionLoginUser.close();
                    System.out.println("Session login user is closed");
                }
            }


        });

        LoginPassword.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode()== KeyCode.ENTER){
                LoginButton.fire();
            }
        });

        LoginUsername.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode()== KeyCode.ENTER){
                LoginButton.fire();
            }
        });


    }
}