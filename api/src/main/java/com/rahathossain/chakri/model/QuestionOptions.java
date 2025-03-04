package com.rahathossain.chakri.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.rahathossain.chakri.exception.InvalidDataException;
import lombok.Getter;

import java.util.Objects;

public final class QuestionOptions {

    @Getter
    public enum Type {
        PARAGRAPH("Paragraph", "open-ended"),
        MULTIPLE_CHOICE("Multiple Choice", "predefined options");

        private final String value;
        private final String description;

        Type(String value, String description) {
            this.value = value;
            this.description = description;
        }

        @JsonCreator
        public static Type fromValue(String value) {
            for (Type type : Type.values()) {
                if (Objects.equals(type.getValue(), value)) {
                    return type;
                }
            }

            throw new InvalidDataException("Type value not recognised");
        }

        @JsonValue
        public String toValue() {
            return value;
        }
    }

    @Getter
    public enum Mode {
        TECHNICAL("Technical", "assessing job skills"),
        BEHAVIORAL("Behavioral", "assessing personality, teamwork, soft skills");

        private final String value;
        private final String description;

        Mode(String value, String description) {
            this.value = value;
            this.description = description;
        }

        @JsonCreator
        public static Mode fromValue(String value) {
            for (Mode mode : Mode.values()) {
                if (Objects.equals(mode.getValue(), value)) {
                    return mode;
                }
            }

            throw new InvalidDataException("Mode value not recognised");
        }

        @JsonValue
        public String toValue() {
            return value;
        }
    }

    @Getter
    public enum Difficulty {
        CREATIVE("Creative", "requires deep thinking, real-world application"),
        STRAIGHT_FORWARD("Straight Forward", "directly tests knowledge");

        private final String value;
        private final String description;

        Difficulty(String value, String description) {
            this.value = value;
            this.description = description;
        }

        @JsonCreator
        public static Difficulty fromValue(String value) {
            for (Difficulty difficulty : Difficulty.values()) {
                if (Objects.equals(difficulty.getValue(), value)) {
                    return difficulty;
                }
            }

            throw new InvalidDataException("Difficulty value not recognised");
        }

        @JsonValue
        public String toValue() {
            return value;
        }
    }
}
