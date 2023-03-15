package com.example.exceptions;

public class EmptyTaskException extends Exception{
    public EmptyTaskException() {
        super();
    }

    public EmptyTaskException(String message) {
        super(message);
    }
}
