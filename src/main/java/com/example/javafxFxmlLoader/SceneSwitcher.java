package com.example.javafxFxmlLoader;

import com.example.Hibernate.HibernateSessionFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneSwitcher {
        public static Stage SceneSwitcher(String PathToFXML) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SceneSwitcher.class.getResource(PathToFXML));

            try {
                loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.setTitle(getTitle(PathToFXML));
            stage.show();


            closeFabrics(stage,PathToFXML);
            return stage;
        }

    public static void closeFabrics(Stage stage, String pathFXML){
        if (!pathFXML.contains("Successful") && !pathFXML.contains("Denied") && !pathFXML.contains("Description")){
            stage.setOnCloseRequest(event -> {
                HibernateSessionFactory.shutdownFactors();
            });
        }
    }

        public static String getTitle (String PathToFXML){
            String title = "";
            switch (PathToFXML) {
                case ("/AddTask.fxml"): title = "Add Task"; break;
                case ("/SignUp.fxml"): title = "Sign Up"; break;
                case ("/Login.fxml"): title = "Login"; break;
                case ("/DescriptionTask.fxml"): title = "Description Task"; break;
                case ("/TaskList.fxml"): title = "To Do List";
                default:break;
            }
            return title;
        }

    }

