package com.rahathossain.chakri.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Answer {

    private String answer;

    public String getAnswer() {
        if (Objects.isNull(answer) || answer.isBlank()) {
            return "No answer";
        }
        return answer;
    }
}