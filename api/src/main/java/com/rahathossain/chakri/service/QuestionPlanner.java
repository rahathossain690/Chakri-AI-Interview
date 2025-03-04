package com.rahathossain.chakri.service;

public abstract class QuestionPlanner implements PromptGenerator {

    private final String jdContent;
    private final String cvContent;

    public QuestionPlanner(String jdContent, String cvContent) {
        this.jdContent = jdContent;
        this.cvContent = cvContent;
    }

    protected String getJdContent() {
        return jdContent;
    }

    protected String getCvContent() {
        return cvContent;
    }
}
