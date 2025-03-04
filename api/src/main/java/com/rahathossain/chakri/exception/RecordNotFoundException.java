package com.rahathossain.chakri.exception;

public class RecordNotFoundException extends RuntimeException {

    public RecordNotFoundException(String message) {
        super(message);
    }

    public RecordNotFoundException() {
        this("Record not found");
    }
}
