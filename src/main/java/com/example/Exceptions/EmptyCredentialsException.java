package com.example.Exceptions;

public class EmptyCredentialsException extends Exception{
    public EmptyCredentialsException() {
        super();
    }

    public EmptyCredentialsException(String message) {
        super(message);
    }
}
