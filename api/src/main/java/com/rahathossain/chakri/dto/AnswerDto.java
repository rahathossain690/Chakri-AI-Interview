package com.rahathossain.chakri.dto;

import com.rahathossain.chakri.model.Answer;
import lombok.Data;

import java.util.List;

@Data
public class AnswerDto {

    private final List<Answer> answers;
}
