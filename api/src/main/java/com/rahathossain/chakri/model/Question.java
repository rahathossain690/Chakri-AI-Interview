package com.rahathossain.chakri.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.util.List;

@Data
@Embeddable
public class Question {

    @JsonProperty("question")
    private String query;

    private List<String> options;

    private QuestionOptions.Type type;

    private QuestionOptions.Mode mode;

    private QuestionOptions.Difficulty difficulty;

    public String getFullQuestion() {
        StringBuilder ret = new StringBuilder(query).append("\n");
        if (!options.isEmpty()) {
            ret.append("options are:\n");
            for (String option : options) {
                ret.append(option).append("\n");
            }
        }

        return ret.toString();
    }
}
