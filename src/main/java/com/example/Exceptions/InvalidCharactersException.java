package com.example.Exceptions;

public class InvalidCharactersException extends Exception{
    public InvalidCharactersException() {
        super();
    }

    public InvalidCharactersException(String message) {
        super(message);
    }
}
