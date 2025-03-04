package com.rahathossain.chakri.controller;

import com.rahathossain.chakri.dto.ExceptionDto;
import com.rahathossain.chakri.exception.InvalidDataException;
import com.rahathossain.chakri.exception.RecordNotFoundException;
import com.rahathossain.chakri.exception.SystemFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleRecordNotFound(RecordNotFoundException ex) {
        return getErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<ExceptionDto> handleInvalidRecordNotFound(InvalidDataException ex) {
        return getErrorResponse(ex.getErrorMessage());
    }

    @ExceptionHandler(SystemFailureException.class)
    public ResponseEntity<ExceptionDto> handleSystemFailure(SystemFailureException ex) {
        return getErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDto> handleException(Exception ex) {
        return getErrorResponse("Something wrong happened");
    }

    private ResponseEntity<ExceptionDto> getErrorResponse(String message) {
        return ResponseEntity.badRequest().body(new ExceptionDto(message));
    }
}
