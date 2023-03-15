package com.example.exceptions;

public class LoginOrPasswordTooLongException extends Exception{
    public LoginOrPasswordTooLongException() {
        super();
    }

    public LoginOrPasswordTooLongException(String message) {
        super(message);
    }
}
