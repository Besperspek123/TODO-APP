package com.example.Exceptions;

public class LoginOrPasswordTooLongException extends Exception{
    public LoginOrPasswordTooLongException() {
        super();
    }

    public LoginOrPasswordTooLongException(String message) {
        super(message);
    }
}
