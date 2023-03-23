package com.example.controller;

import com.example.exceptions.EmptyTaskException;
import com.example.hibernate.HibernateSessionFactory;
import com.example.entitiesDatabase.CurrentTask;
import com.example.controller.constantsNotification.ErrorConstants;
import com.example.javafxFxmlLoader.JavaFx;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import org.hibernate.Session;

public class AddTaskController {

    @FXML
    private TextField descriptionTask;

    @FXML
    private TextField task;

    @FXML
    private Button buttonSaveTask;

    @FXML
    private Button buttonBack;

    @FXML
    void initialize() {
        setupAddTaskButton();
        setupBackButton();
        handleEnterPressKey();
    }


    private void setupAddTaskButton(){
        buttonSaveTask.setOnAction(actionEvent -> {
            Session sessionSaveNewTask = null;
            try {
                sessionSaveNewTask = HibernateSessionFactory.getCurrentSessionCurrentTask();
                sessionSaveNewTask.beginTransaction();

                CurrentTask currentTask = new CurrentTask(task.getText(),
                        descriptionTask.getText(),
                        LoginController.getCurrentUserLogin());

                if (task.getText().isEmpty()){
                    throw new EmptyTaskException("The User is trying to save an empty Task");
                }
                sessionSaveNewTask.save(currentTask);
                sessionSaveNewTask.getTransaction().commit();
                sessionSaveNewTask.close();
                buttonSaveTask.getScene().getWindow().hide();
                JavaFx.SceneSwitcher("/TaskList.fxml");
            }
            catch (EmptyTaskException e){
                System.out.println(e.getMessage());
                JavaFx.showInputErrorNotification(ErrorConstants.ERROR_SAVE_EMPTY_TASK);
            }
            finally {
                if (sessionSaveNewTask != null && sessionSaveNewTask.isOpen()) {
                    sessionSaveNewTask.close();
                }
            }
        });
    }

    private void setupBackButton() {
        buttonBack.setOnAction(actionEvent -> {
            buttonBack.getScene().getWindow().hide();
            JavaFx.SceneSwitcher("/TaskList.fxml");
        });
    }

    private void handleEnterPressKey() {
        task.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode()== KeyCode.ENTER){
                buttonSaveTask.fire();
            }
        });

        descriptionTask.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode()== KeyCode.ENTER){
                buttonSaveTask.fire();
            }
        });
    }

}