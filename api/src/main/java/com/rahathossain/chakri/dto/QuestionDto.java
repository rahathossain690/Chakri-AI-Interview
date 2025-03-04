package com.rahathossain.chakri.dto;

import com.rahathossain.chakri.model.Question;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Data
public class QuestionDto {

    private final boolean ready;

    private final List<Question> questions;

    public QuestionDto() {
        ready = false;
        questions = Collections.emptyList();
    }

    public QuestionDto(List<Question> questions) {
        assert Objects.nonNull(questions);

        ready = true;
        this.questions = questions;
    }
}
