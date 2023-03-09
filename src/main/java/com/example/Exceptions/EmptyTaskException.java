package com.example.Exceptions;

public class EmptyTaskException extends Exception{
    public EmptyTaskException() {
        super();
    }

    public EmptyTaskException(String message) {
        super(message);
    }
}
