package com.example.exceptions;

public class EmptyCredentialsException extends Exception{
    public EmptyCredentialsException() {
        super();
    }

    public EmptyCredentialsException(String message) {
        super(message);
    }
}
