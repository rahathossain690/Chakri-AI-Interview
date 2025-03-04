package com.rahathossain.chakri.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rahathossain.chakri.dto.AnswerDto;
import com.rahathossain.chakri.dto.EvaluationScoreDto;
import com.rahathossain.chakri.dto.QuestionDto;
import com.rahathossain.chakri.exception.InvalidDataException;
import com.rahathossain.chakri.exception.SystemFailureException;
import com.rahathossain.chakri.model.Answer;
import com.rahathossain.chakri.model.EvaluationScore;
import com.rahathossain.chakri.model.QnAEntry;
import com.rahathossain.chakri.model.Question;
import com.rahathossain.chakri.repository.QnAEntryRepository;
import com.rahathossain.chakri.util.TrimmingObjectMapper;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class ChakriService {

    private final LLMService llmService;
    private final QnAEntryRepository qnaEntryRepository;

    public ChakriService(LLMService llmService,
                         QnAEntryRepository qnaEntryRepository) {
        this.llmService = llmService;
        this.qnaEntryRepository = qnaEntryRepository;
    }

    public void validateJdAndCv(MultipartFile jd, MultipartFile cv) {
        if (Objects.isNull(jd)) {
            throw new InvalidDataException("Job Description cannot be empty");
        }

        if (Objects.isNull(cv)) {
            throw new InvalidDataException("CV cannot be empty");
        }

        try (InputStream inputStream = jd.getInputStream(); PDDocument document = PDDocument.load(inputStream)) {
        } catch (IOException e) {
            throw new InvalidDataException("Job Description is not valid pdf");
        }

        try (InputStream inputStream = cv.getInputStream(); PDDocument document = PDDocument.load(inputStream)) {
        } catch (IOException e) {
            throw new InvalidDataException("CV is not valid pdf");
        }
    }

    public QnAEntry createNewQnAEntry(MultipartFile jd, MultipartFile cv) {
        QnAEntry qnaEntry = new QnAEntry();

        qnaEntry.setJdContent(readPdfContent(jd));
        qnaEntry.setCvContent(readPdfContent(cv));
        qnaEntry.setCreatedAt(new Date());

        return qnaEntry;
    }

    public void runBackgroundQuestionJob(QnAEntry qnaEntry) {
        QuestionPlanner planner = new SimpleQuestionPlanner(qnaEntry.getJdContent(), qnaEntry.getCvContent());

        llmService.runPromptAsync(planner.generatePrompt()).thenAccept(result -> {
            String jsonStr = extractJson(result, "[", "]");
            List<Question> questions = parseJsonToQuestions(jsonStr);
            qnaEntry.setQuestions(questions);

            qnaEntryRepository.saveOrUpdate(qnaEntry);
        });
    }

    public QuestionDto getQuestionDto(UUID id) {
        QnAEntry qnaEntry = qnaEntryRepository.get(id);

        if (Objects.isNull(qnaEntry.getQuestions()) || qnaEntry.getQuestions().isEmpty()) {
            return new QuestionDto();
        }

        return new QuestionDto(qnaEntry.getQuestions());
    }

    public void processAnswer(UUID id, AnswerDto answerDto) {
        QnAEntry qnaEntry = qnaEntryRepository.get(id);

        validateQuestionAnswer(qnaEntry.getQuestions(), answerDto.getAnswers());
        if (!qnaEntry.getAnswers().isEmpty()) {
            return;
        }

        List<EvaluationPlanner.QnAForEvaluation> qnaForEvaluations = new LinkedList<>();
        for (int i = 0; i < qnaEntry.getQuestions().size(); i++) {
            qnaForEvaluations.add(new EvaluationPlanner.QnAForEvaluation(
                    qnaEntry.getQuestions().get(i).getFullQuestion(),
                    answerDto.getAnswers().get(i).getAnswer()
            ));
        }

        EvaluationPlanner planner = new SimpleEvaluationPlanner(qnaEntry.getJdContent(), qnaEntry.getCvContent(), qnaForEvaluations);
        llmService.runPromptAsync(planner.generatePrompt()).thenAccept(result -> {
            String jsonStr = extractJson(result, "{", "}");
            EvaluationScore evaluationScore = parseJsonToEvaluationScore(jsonStr);
            qnaEntry.setEvaluationScore(evaluationScore);

            qnaEntryRepository.saveOrUpdate(qnaEntry);
        });
    }

    public EvaluationScoreDto getEvaluationScoreDto(UUID id) {
        QnAEntry qnaEntry = qnaEntryRepository.get(id);

        if (Objects.isNull(qnaEntry.getEvaluationScore())) {
            return new EvaluationScoreDto();
        }

        return new EvaluationScoreDto(qnaEntry.getEvaluationScore());
    }

    private String extractJson(String result, final String firstChar, final String lastChar) {
        int startIndex = result.indexOf(firstChar);
        int endIndex = result.lastIndexOf(lastChar);

        if (startIndex == -1 || endIndex == -1 || startIndex >= endIndex) {
            throw new SystemFailureException("LLM output does not contain valid JSON");
        }

        result = result.substring(startIndex, endIndex + 1).trim();

        try {
            new TrimmingObjectMapper().readTree(result);
            return result;

        } catch (Exception e) {
            throw new SystemFailureException("LLM output parsing failed: " + e.getMessage());
        }
    }

    private List<Question> parseJsonToQuestions(String jsonString) {
        ObjectMapper objectMapper = new TrimmingObjectMapper();

        try {
            return objectMapper.readValue(jsonString, new TypeReference<>() {
            });

        } catch (IOException e) {
            throw new SystemFailureException("Unable to read questions");
        }
    }

    private EvaluationScore parseJsonToEvaluationScore(String jsonStr) {
        ObjectMapper objectMapper = new TrimmingObjectMapper();

        try {
            return objectMapper.readValue(jsonStr, EvaluationScore.class);

        } catch (IOException e) {
            throw new SystemFailureException("Unable to read evaluation score");
        }
    }

    private String readPdfContent(MultipartFile pdf) {
        try (InputStream inputStream = pdf.getInputStream(); PDDocument document = PDDocument.load(inputStream)) {
            return new PDFTextStripper().getText(document);

        } catch (Exception e) {
            throw new SystemFailureException("Unable to parse pdf");
        }
    }

    private void validateQuestionAnswer(List<Question> questions, List<Answer> answers) {
        if (Objects.isNull(answers) || answers.isEmpty()) {
            throw new InvalidDataException("No answer found");
        }

        if (Objects.isNull(questions) || questions.isEmpty()) {
            throw new InvalidDataException("Unable to match question with answer");
        }

        if (answers.size() != questions.size()) {
            throw new InvalidDataException("Answer count do not match with question count");
        }

        questions.forEach(question -> {
            if (Objects.isNull(question.getQuery())) {
                throw new InvalidDataException("Question missing");
            }
        });

        answers.forEach(answer -> {
            if (Objects.isNull(answer.getAnswer())) {
                throw new InvalidDataException("Answer missing");
            }
        });
    }
}
