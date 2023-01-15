package com.example.application.exceptions;

public class NoDataFoundException extends RuntimeException {

    public NoDataFoundException(String massage) {
        super(massage);
    }
}
