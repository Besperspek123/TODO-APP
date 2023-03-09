package com.example.springbootjavafxapplicationtodolist;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootJavaFxApplicationToDoListApplication {

    public static void main(String[] args) {

        Application.launch(com.example.Main.class,args);
    }

}
