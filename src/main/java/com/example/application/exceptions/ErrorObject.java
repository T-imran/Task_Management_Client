package com.example.application.exceptions;

import lombok.Data;

@Data
public class ErrorObject {
    private Integer statusCode;
    private String massage;
    private Long timeTemp;
}
