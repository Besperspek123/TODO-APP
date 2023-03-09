package com.example;

import com.example.javafxFxmlLoader.SceneSwitcher;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application{

    public static void main(String[] args) {

        launch(args);

    }

    @Override
    public void start(Stage stage) {

        Stage LoginStage = SceneSwitcher.SceneSwitcher("/Login.fxml");




    }

}