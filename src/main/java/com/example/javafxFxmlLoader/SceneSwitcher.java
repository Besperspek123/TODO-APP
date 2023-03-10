package com.example.javafxFxmlLoader;

import com.example.Hibernate.HibernateSessionFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
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

    public static Stage showInputErrorNotification(String Error) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(SceneSwitcher.class.getResource("/ErrorNotification.fxml"));

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Parent root = loader.getRoot();

        AnchorPane anchorPane =(AnchorPane) root.lookup("#windowErrorNotification");



        Text errorText = (Text) root.lookup("#textErrorNotification");
        errorText.setText(Error);
        double textWidth = errorText.getBoundsInLocal().getWidth();
        double textHeight = errorText.getBoundsInLocal().getHeight();

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);

        stage.setMinHeight(textHeight + 65);

        stage.show();

        return stage;
    }

    public static Stage showInputSuccessfulNotification(String SuccessfulMessage) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(SceneSwitcher.class.getResource("/SuccessfulNotification.fxml"));

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Parent root = loader.getRoot();

        AnchorPane anchorPane =(AnchorPane) root.lookup("#windowSuccessfulNotification");



        Text successfulText = (Text) root.lookup("#textSuccessfulNotification");
        successfulText.setText(SuccessfulMessage);
        double textWidth = successfulText.getBoundsInLocal().getWidth();
        double textHeight = successfulText.getBoundsInLocal().getHeight();

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);

        stage.setMinHeight(textHeight + 65);

        stage.show();

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

