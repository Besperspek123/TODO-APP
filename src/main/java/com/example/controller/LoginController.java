package com.example.controller;

import com.example.exceptions.EmptyCredentialsException;
import com.example.exceptions.UserNotFoundException;
import com.example.hibernate.HibernateSessionFactory;
import com.example.objectsDataBase.User;
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

public class LoginController {

    private static User currentLoginUser;

    public static String getCurrentUserLogin() {
        return currentLoginUser.getLoginUsername();
    }

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField loginPassword;

    @FXML
    private TextField loginUsername;

    @FXML
    private Button signUpButton;

    @FXML
    private Button clearDataAndUsersButton;


    @FXML
    void initialize() {
        setupLoginButton();
        setupSignUpButton();
        setupClearUsersAndTasksButton();
        handleEnterPressKey();
    }


    private void setupLoginButton() {
        loginButton.setOnAction(actionEvent -> {


            Session sessionLoginUser = null;

            try {

                if (loginUsername.getText().isEmpty() || loginPassword.getText().isEmpty()) {
                    throw new EmptyCredentialsException("The user did not enter a username or password");
                }

                sessionLoginUser = HibernateSessionFactory.getCurrentSessionUser();
                sessionLoginUser.beginTransaction();
                System.out.println("Session login user is open");

                String sqlQuery = "from User where loginUsername =:login and loginPassword =:password";
                Query checkUserQuery = sessionLoginUser.createQuery(sqlQuery);
                checkUserQuery.setParameter("login", loginUsername.getText());
                checkUserQuery.setParameter("password", loginPassword.getText());

                List<User> usersForQueryList = checkUserQuery.getResultList();

                if (!usersForQueryList.isEmpty()) {

                    currentLoginUser = usersForQueryList.get(0);

                    loginButton.getScene().getWindow().hide();
                    JavaFx.SceneSwitcher("/TaskList.fxml");

                } else {
                    throw new UserNotFoundException("User not found");
                }
            } catch (UserNotFoundException e) {
                System.out.println(e.getMessage());
                JavaFx.showInputErrorNotification(ErrorConstants.ERROR_LOGIN_OR_PASSWORD_INCORRECT);

            } catch (EmptyCredentialsException e) {
                System.out.println(e.getMessage());
                JavaFx.showInputErrorNotification(ErrorConstants.ERROR_LOGIN_OR_PASSWORD_EMPTY);
            } finally {
                if (sessionLoginUser != null && sessionLoginUser.isOpen()) {
                    sessionLoginUser.close();
                    System.out.println("Session login user is closed");
                }
            }


        });
    }

    private void setupClearUsersAndTasksButton() {
        clearDataAndUsersButton.setOnAction(actionEvent -> deleteUsersAndTasksInDataBase());
    }

    private void setupSignUpButton() {
        signUpButton.setOnAction(actionEvent -> {

            signUpButton.getScene().getWindow().hide();
            JavaFx.SceneSwitcher("/SignUp.fxml");

        });

    }


    public void deleteUsersAndTasksInDataBase() {
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
            JavaFx.showInputSuccessfulNotification(SuccessfulConstants.SUCCESSFUL_DELETE_ALL_USER);

        } finally {
            if (sessionDeleteAllTaskFromAllUser != null && sessionDeleteAllUsers.isOpen()) {
                sessionDeleteAllTaskFromAllUser.close();
            }
            if (sessionDeleteAllCompletedTaskFromAllUser != null && sessionDeleteAllCompletedTaskFromAllUser.isOpen()) {
                sessionDeleteAllCompletedTaskFromAllUser.close();
            }
        }
    }

    private void handleEnterPressKey() {
        loginPassword.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                loginButton.fire();
            }
        });

        loginUsername.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                loginButton.fire();
            }
        });
    }
}