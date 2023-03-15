package com.example.exceptions;

public class TaskAlreadyCompletedException extends Exception{
    public TaskAlreadyCompletedException() {
        super();
    }

    public TaskAlreadyCompletedException(String message) {
        super(message);
    }
}
