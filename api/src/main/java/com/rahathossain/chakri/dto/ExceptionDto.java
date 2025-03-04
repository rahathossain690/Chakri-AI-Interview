package com.rahathossain.chakri.dto;

import lombok.Data;

@Data
public class ExceptionDto {

    private final String error;

    public ExceptionDto(String error) {
        this.error = error;
    }
}
