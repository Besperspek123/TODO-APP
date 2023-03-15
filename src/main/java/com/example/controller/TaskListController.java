package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import com.example.hibernate.HibernateSessionFactory;
import com.example.entitiesDatabase.CompletedTask;
import com.example.entitiesDatabase.CurrentTask;
import com.example.javafxFxmlLoader.JavaFx;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import org.hibernate.Session;

import javax.persistence.Query;

public class TaskListController {

    private List<CurrentTask> currentTaskListForCurrentUser = new ArrayList<>();
    private List<CompletedTask> completedTaskListForCurrentUser = new ArrayList<>();
    private static String currentTaskDescription = null;

    public static String getCurrentTaskDescription(){
       return currentTaskDescription;
    }
    @FXML
    private Text textUnderButtonAddTask;

    @FXML
    private AnchorPane panelButtonTask;

    @FXML
    private Button backToLoginMenu;

    @FXML
    private ImageView addFirstTaskButton;

    @FXML
    private SplitMenuButton changeTaskListMenuButton;

    @FXML
    private MenuItem changeToCompletedTasksList;

    @FXML
    private MenuItem changeToCurrentTasksList;

    @FXML
    private Button completeTaskButton;

    @FXML
    private Button descriptionTaskButton;

    @FXML
    private Button removeTaskButton;

    @FXML
    private Button addTaskButton;

    @FXML
    private ListView<String> listViewCurrentTasks;

    @FXML
    private ListView<String> listViewCompletedTasks;



    @FXML
    void initialize() {


        getCurrentTaskList();
        getCompletedTaskList();

        showCurrentTasks();
        showCompletedTasks();

        setupAddTaskButtonWhenUserNotHaveTasks();
        setupAddTaskButtonWhenUserHaveTasks();
        setupChangeTypeTasksButton();

        setupDescriptionTaskButton();
        setupRemoveTaskButton();
        setupCompleteTaskButton();

        setupBackToLoginButton();

    }

    private void getCompletedTaskList() {
        Session sessionGetCompletedTasks = null;
        try{
            sessionGetCompletedTasks = HibernateSessionFactory.getCurrentSessionCompletedTask();
            sessionGetCompletedTasks.beginTransaction();

            String sqlQuery = "from CompletedTask where user =:user";
            Query completedTasksForCurrentUser = sessionGetCompletedTasks.createQuery(sqlQuery);
            completedTasksForCurrentUser.setParameter("user", LoginController.getCurrentUserLogin());

            completedTaskListForCurrentUser = completedTasksForCurrentUser.getResultList();

        }
        finally {
            if (sessionGetCompletedTasks != null && sessionGetCompletedTasks.isOpen()) {
                sessionGetCompletedTasks.close();
            }
        }

    }

    private void showCompletedTasks() {

        for (CompletedTask CompletedTask : completedTaskListForCurrentUser
        ) {
            listViewCompletedTasks.getItems().add(CompletedTask.getTask());
        }

    }

    private void setupChangeTypeTasksButton() {
        changeToCurrentTasksList.setOnAction(actionEvent -> {

            if(listViewCurrentTasks.getItems().isEmpty()){
                addFirstTaskButton.setVisible(true);
                textUnderButtonAddTask.setVisible(true);
            }
            completeTaskButton.setDisable(false);
            listViewCurrentTasks.setVisible(true);
            listViewCompletedTasks.setVisible(false);
            changeTaskListMenuButton.setText("Current Tasks");

        });
        changeToCompletedTasksList.setOnAction(actionEvent -> {
            if(!listViewCompletedTasks.getItems().isEmpty()){
                addFirstTaskButton.setVisible(false);
                textUnderButtonAddTask.setVisible(false);
                panelButtonTask.setVisible(true);
                addTaskButton.setVisible(true);
            }
            completeTaskButton.setDisable(true);
            listViewCurrentTasks.setVisible(false);
            listViewCompletedTasks.setVisible(true);
            changeTaskListMenuButton.setText("Completed Tasks");
        });
    }

    private void setupCompleteTaskButton() {
        completeTaskButton.setOnAction(actionEvent -> {
            completedTask();
        });

    }

    private void setupRemoveTaskButton() {

        removeTaskButton.setOnAction(actionEvent -> {
            removeTask();
        });

    }

    private void setupDescriptionTaskButton() {

        descriptionTaskButton.setOnAction(actionEvent -> {
            currentTaskDescription = getDescriptionTaskText();
            JavaFx.SceneSwitcher("/DescriptionTask.fxml");
        });

    }

    private void getCurrentTaskList() {
        Session sessionGetCurrentTasks = null;
        try {
            sessionGetCurrentTasks = HibernateSessionFactory.getCurrentSessionCurrentTask();
            sessionGetCurrentTasks.beginTransaction();

            String sqlQuery = "from CurrentTask where user =:user";
            Query tasksForCurrentUser = sessionGetCurrentTasks.createQuery(sqlQuery);
            tasksForCurrentUser.setParameter("user", LoginController.getCurrentUserLogin());

            currentTaskListForCurrentUser = tasksForCurrentUser.getResultList();

        } finally {
            if (sessionGetCurrentTasks != null && sessionGetCurrentTasks.isOpen()) {
                sessionGetCurrentTasks.close();
            }
        }

    }

    private void setupBackToLoginButton() {
        backToLoginMenu.setOnAction(actionEvent -> {
            backToLoginMenu.getScene().getWindow().hide();
            JavaFx.SceneSwitcher("/Login.fxml");
        });
    }

    private void showCurrentTasks() {
        addTaskButton.setVisible(false);
        for (CurrentTask currentTask : currentTaskListForCurrentUser
        ) {
            listViewCurrentTasks.getItems().add(currentTask.getTask());
        }

        if (currentTaskListForCurrentUser.isEmpty()) {
            panelButtonTask.setVisible(false);
            listViewCurrentTasks.setVisible(false);
            listViewCompletedTasks.setVisible(false);
        } else {
            addFirstTaskButton.setVisible(false);
            textUnderButtonAddTask.setVisible(false);
            listViewCurrentTasks.setVisible(true);
            addTaskButton.setVisible(true);
        }
    }

    private void setupAddTaskButtonWhenUserHaveTasks() {

        addTaskButton.setOnAction(actionEvent -> {
            switchToAddTaskStage();
        });

    }

    private void setupAddTaskButtonWhenUserNotHaveTasks() {


        addFirstTaskButton.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            addFirstTaskButton.setStyle("-fx-opacity: 50%;-fx-rotate: 45");
        });
        addFirstTaskButton.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            addFirstTaskButton.setStyle("-fx-opacity: 100%;-fx-rotate: 0");
        });
        addFirstTaskButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {

            switchToAddTaskStage();
            addFirstTaskButton.setVisible(false);
            textUnderButtonAddTask.setVisible(false);
            listViewCurrentTasks.setVisible(true);
            addTaskButton.setVisible(true);


        });

    }


    private void switchToAddTaskStage() {
        addFirstTaskButton.getScene().getWindow().hide();
        JavaFx.SceneSwitcher("/AddTask.fxml");
    }

    private void removeTask() {

        Session sessionCompletedOrCurrentRemoveTask = null;
        try {

            if (listViewCurrentTasks.isVisible()){
                sessionCompletedOrCurrentRemoveTask = HibernateSessionFactory.getCurrentSessionCurrentTask();
                sessionCompletedOrCurrentRemoveTask.beginTransaction();
                String selectedItem = listViewCurrentTasks.getSelectionModel().getSelectedItem();
                String sqlQueryDeleteTask = "from CurrentTask where task =:task and user=:currentUser";
                Query tasksForCurrentUser = sessionCompletedOrCurrentRemoveTask.createQuery(sqlQueryDeleteTask);
                tasksForCurrentUser.setParameter("task", selectedItem);
                tasksForCurrentUser.setParameter("currentUser", LoginController.getCurrentUserLogin());
                List<CurrentTask> currentTaskForDelete = tasksForCurrentUser.getResultList();
                System.out.println(currentTaskForDelete.toString());
                for (CurrentTask currentTask : currentTaskForDelete
                ) {
                    sessionCompletedOrCurrentRemoveTask.remove(currentTask);
                }
                sessionCompletedOrCurrentRemoveTask.getTransaction().commit();

                listViewCurrentTasks.getItems().remove(selectedItem);
            }
            else {
                sessionCompletedOrCurrentRemoveTask = HibernateSessionFactory.getCurrentSessionCompletedTask();
                sessionCompletedOrCurrentRemoveTask.beginTransaction();
                String selectedItem = listViewCompletedTasks.getSelectionModel().getSelectedItem();
                String sqlQueryDeleteTask = "from CompletedTask where task =:task and user=:currentUser";
                Query tasksForCurrentUser = sessionCompletedOrCurrentRemoveTask.createQuery(sqlQueryDeleteTask);
                tasksForCurrentUser.setParameter("task", selectedItem);
                tasksForCurrentUser.setParameter("currentUser", LoginController.getCurrentUserLogin());
                List<CompletedTask> currentTaskForDelete = tasksForCurrentUser.getResultList();
                System.out.println(currentTaskForDelete.toString());
                for (CompletedTask currentTask : currentTaskForDelete
                ) {
                    sessionCompletedOrCurrentRemoveTask.remove(currentTask);
                }
                sessionCompletedOrCurrentRemoveTask.getTransaction().commit();

                listViewCompletedTasks.getItems().remove(selectedItem);
            }


        } finally {
            if (sessionCompletedOrCurrentRemoveTask != null && sessionCompletedOrCurrentRemoveTask.isOpen()) {
                sessionCompletedOrCurrentRemoveTask.close();
            }
        }

    }

    private void completedTask() {

        Session sessionGetAndDeleteCurrentTaskForAddToCompletedTasks = null;
        Session sessionAddCompletedTask = null;
        List<CurrentTask> currentTaskForAddCompleted;

        try {
            sessionGetAndDeleteCurrentTaskForAddToCompletedTasks = HibernateSessionFactory.getCurrentSessionCurrentTask();
            sessionGetAndDeleteCurrentTaskForAddToCompletedTasks.beginTransaction();
            String selectedItem = listViewCurrentTasks.getSelectionModel().getSelectedItem();
            String sqlQueryCompletedTask = "from CurrentTask where task =:task and user=:currentUser";
            Query tasksForCurrentUser = sessionGetAndDeleteCurrentTaskForAddToCompletedTasks.createQuery(sqlQueryCompletedTask);
            tasksForCurrentUser.setParameter("task", selectedItem);
            tasksForCurrentUser.setParameter("currentUser", LoginController.getCurrentUserLogin());
            currentTaskForAddCompleted = tasksForCurrentUser.getResultList();
            for (CurrentTask currentTask : currentTaskForAddCompleted
            ) {
                sessionGetAndDeleteCurrentTaskForAddToCompletedTasks.remove(currentTask);
                listViewCompletedTasks.getItems().add(currentTask.getTask());
                listViewCurrentTasks.getItems().remove(currentTask.getTask());
            }
            sessionGetAndDeleteCurrentTaskForAddToCompletedTasks.getTransaction().commit();
        }
        finally {
            if (sessionGetAndDeleteCurrentTaskForAddToCompletedTasks != null && sessionGetAndDeleteCurrentTaskForAddToCompletedTasks.isOpen()) {
                sessionGetAndDeleteCurrentTaskForAddToCompletedTasks.close();
            }
        }

        try {
            sessionAddCompletedTask = HibernateSessionFactory.getCurrentSessionCompletedTask();
            sessionAddCompletedTask.beginTransaction();
            for (CurrentTask currentTask : currentTaskForAddCompleted
            ) {
                CompletedTask completedTask = new CompletedTask(currentTask.getTask(),
                        currentTask.getDescription(),
                        currentTask.getUser());
                sessionAddCompletedTask.save(completedTask);
            }
            sessionAddCompletedTask.getTransaction().commit();

        }
        finally {
            if (sessionAddCompletedTask != null && sessionAddCompletedTask.isOpen()) {
                sessionAddCompletedTask.close();
            }

        }

    }

    private String getDescriptionTaskText() {

        Session sessionGetDescriptionTask = null;
        try {
                if(listViewCurrentTasks.isVisible()){
                    sessionGetDescriptionTask = HibernateSessionFactory.getCurrentSessionCurrentTask();
                }
                else sessionGetDescriptionTask = HibernateSessionFactory.getCurrentSessionCompletedTask();
            sessionGetDescriptionTask.beginTransaction();
            String selectedItem = null;
            String sqlQueryDeleteTask = null;
            if(listViewCurrentTasks.isVisible()){
               selectedItem =  listViewCurrentTasks.getSelectionModel().getSelectedItem();
                sqlQueryDeleteTask = "from CurrentTask where task =:task and user=:currentUser";
            }
            else {
                selectedItem = listViewCompletedTasks.getSelectionModel().getSelectedItem();
                sqlQueryDeleteTask = "from CompletedTask where task =:task and user=:currentUser";}

            Query tasksForCurrentUser = sessionGetDescriptionTask.createQuery(sqlQueryDeleteTask);
            tasksForCurrentUser.setParameter("task", selectedItem);
            tasksForCurrentUser.setParameter("currentUser", LoginController.getCurrentUserLogin());
            String Description = "";
            if(listViewCurrentTasks.isVisible()){
                List<CurrentTask> currentTaskForDelete = tasksForCurrentUser.getResultList();
                Description = currentTaskForDelete.get(0).getDescription();

            }
            else{ List<CompletedTask> currentTaskForDelete = tasksForCurrentUser.getResultList();
                Description = currentTaskForDelete.get(0).getDescription();}
            return Description;


        } finally {
            if (sessionGetDescriptionTask != null && sessionGetDescriptionTask.isOpen()) {
                sessionGetDescriptionTask.close();
            }
        }

    }


}
