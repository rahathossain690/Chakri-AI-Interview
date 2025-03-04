package com.rahathossain.chakri.service;

import lombok.Getter;

import java.text.MessageFormat;
import java.util.List;

public abstract class EvaluationPlanner implements PromptGenerator {

    private final String jdContent;
    private final String cvContent;
    private final List<QnAForEvaluation> qnaForEvaluations;

    public EvaluationPlanner(String jdContent, String cvContent, List<QnAForEvaluation> qnaForEvaluations) {
        this.jdContent = jdContent;
        this.cvContent = cvContent;
        this.qnaForEvaluations = qnaForEvaluations;
    }

    protected String getQuestionsWithAnswers() {
        StringBuilder sb = new StringBuilder();

        for (QnAForEvaluation qnaForEvaluation : qnaForEvaluations) {
            sb.append(qnaForEvaluation.getQuestionAnswer()).append("\n");
        }

        return sb.toString();
    }

    protected String getJdContent() {
        return jdContent;
    }

    protected String getCvContent() {
        return cvContent;
    }

    @Getter
    public static class QnAForEvaluation {

        private final String fullQuestion;

        private final String fullAnswer;

        public QnAForEvaluation(String fullQuestion, String fullAnswer) {
            this.fullQuestion = fullQuestion;
            this.fullAnswer = fullAnswer;
        }

        public String getQuestionAnswer() {
            return MessageFormat.format(
                    """
                            Interview question:
                            {0}
                            
                            Interviewee answer:
                            {1}
                            """,
                    fullQuestion,
                    fullAnswer
            );
        }
    }
}
