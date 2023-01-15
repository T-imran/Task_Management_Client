package com.example.application.exceptions;


public class ResourceNotFoundException extends RuntimeException  {
    public ResourceNotFoundException(String massage) {
        super(massage);
    }
}
