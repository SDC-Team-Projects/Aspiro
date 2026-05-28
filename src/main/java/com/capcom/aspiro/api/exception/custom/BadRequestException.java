package com.capcom.aspiro.api.exception.custom;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}