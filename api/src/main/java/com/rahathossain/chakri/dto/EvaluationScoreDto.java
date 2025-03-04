package com.rahathossain.chakri.dto;

import com.rahathossain.chakri.model.EvaluationScore;
import lombok.Data;

import java.util.Objects;

@Data
public class EvaluationScoreDto {

    private final boolean ready;

    private final EvaluationScore evaluationScore;

    public EvaluationScoreDto() {
        ready = false;
        evaluationScore = null;
    }

    public EvaluationScoreDto(EvaluationScore evaluationScore) {
        assert Objects.nonNull(evaluationScore);

        ready = true;
        this.evaluationScore = evaluationScore;
    }
}
