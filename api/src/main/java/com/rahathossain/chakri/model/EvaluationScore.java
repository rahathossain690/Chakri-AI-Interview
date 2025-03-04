package com.rahathossain.chakri.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class EvaluationScore {

    private static final double relevanceToJdWeight = 30;
    private static final double technicalProficiencyWeight = 25;
    private static final double communicationSkillWeight = 15;
    private static final double problemSolvingAbilityWeight = 15;
    private static final double experienceWeight = 5;
    private static final double achievementWeight = 5;
    private static final double culturalAndSoftFitWeight = 5;

    @Size(min = 0, max = 100)
    private double relevanceToJd;

    @Size(min = 0, max = 100)
    private double technicalProficiency;

    @Size(min = 0, max = 100)
    private double communicationSkill;

    @Size(min = 0, max = 100)
    private double problemSolvingAbility;

    @Size(min = 0, max = 100)
    private double experience;

    @Size(min = 0, max = 100)
    private double achievement;

    @Size(min = 0, max = 100)
    private double culturalAndSoftFit;

    @JsonProperty("score")
    public double getScore() {
        double score = 0;

        score += (relevanceToJd / 100) * (relevanceToJdWeight / 100);
        score += (technicalProficiency / 100) * (technicalProficiencyWeight / 100);
        score += (communicationSkill / 100) * (communicationSkillWeight / 100);
        score += (problemSolvingAbility / 100) * (problemSolvingAbilityWeight / 100);
        score += (experience / 100) * (experienceWeight / 100);
        score += (achievement / 100) * (achievementWeight / 100);
        score += (culturalAndSoftFit / 100) * (culturalAndSoftFitWeight / 100);

        score *= 100;

        score = Double.parseDouble(new DecimalFormat("#.00").format(score));

        return score;
    }
}
