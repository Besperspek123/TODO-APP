package com.example.controller;

import com.example.Exceptions.EmptyTaskException;
import com.example.Hibernate.HibernateSessionFactory;
import com.example.ObjectsDataBase.CurrentTask;
import com.example.controller.constantsNotification.ErrorConstants;
import com.example.javafxFxmlLoader.SceneSwitcher;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import org.hibernate.Session;

public class AddTaskController {

    @FXML
    private TextField DescriptionTask;

    @FXML
    private TextField Task;

    @FXML
    private Button buttonSaveTask;

    @FXML
    private Button buttonBack;

    @FXML
    void initialize() {
        setupAddTaskButton();
        setupBackButton();
    }

    private void setupBackButton() {
        buttonBack.setOnAction(actionEvent -> {
            buttonBack.getScene().getWindow().hide();
            SceneSwitcher.SceneSwitcher("/TaskList.fxml");
        });
    }

    public void setupAddTaskButton(){
        buttonSaveTask.setOnAction(actionEvent -> {
            Session sessionSaveNewTask = null;
            try {
                sessionSaveNewTask = HibernateSessionFactory.getCurrentSessionCurrentTask();
                sessionSaveNewTask.beginTransaction();

                CurrentTask currentTask = new CurrentTask(Task.getText(),
                        DescriptionTask.getText(),
                        LoginController.getCurrentUserLogin());

                if (Task.getText().isEmpty()){
                    throw new EmptyTaskException("The User is trying to save an empty Task");
                }
                sessionSaveNewTask.save(currentTask);
                sessionSaveNewTask.getTransaction().commit();
                sessionSaveNewTask.close();
                buttonSaveTask.getScene().getWindow().hide();
                SceneSwitcher.SceneSwitcher("/TaskList.fxml");
            }
            catch (EmptyTaskException e){
                System.out.println(e.getMessage());
                SceneSwitcher.showInputErrorNotification(ErrorConstants.ERROR_SAVE_EMPTY_TASK);
            }
            finally {
                if (sessionSaveNewTask != null && sessionSaveNewTask.isOpen()) {
                    sessionSaveNewTask.close();
                }
            }


        });

        Task.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode()== KeyCode.ENTER){
                buttonSaveTask.fire();
            }
        });

        DescriptionTask.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode()== KeyCode.ENTER){
                buttonSaveTask.fire();
            }
        });
    }

}