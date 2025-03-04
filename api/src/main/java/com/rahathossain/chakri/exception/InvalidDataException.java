package com.rahathossain.chakri.exception;

import lombok.Getter;

@Getter
public class InvalidDataException extends RuntimeException {

    private final String errorMessage;

    public InvalidDataException(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
