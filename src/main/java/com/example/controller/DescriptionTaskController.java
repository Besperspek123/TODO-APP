package com.example.controller;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class DescriptionTaskController {

    @FXML
    private Text description;

    @FXML
    void initialize() {
        description.setText(TaskListController.getCurrentTaskDescription());

    }

}