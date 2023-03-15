package com.example.exceptions;

public class InvalidCharactersException extends Exception{
    public InvalidCharactersException() {
        super();
    }

    public InvalidCharactersException(String message) {
        super(message);
    }
}
