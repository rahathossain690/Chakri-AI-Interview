package com.rahathossain.chakri.controller;

import com.rahathossain.chakri.dto.AnswerDto;
import com.rahathossain.chakri.dto.EvaluationScoreDto;
import com.rahathossain.chakri.dto.QuestionDto;
import com.rahathossain.chakri.model.JSONData;
import com.rahathossain.chakri.model.QnAEntry;
import com.rahathossain.chakri.repository.QnAEntryRepository;
import com.rahathossain.chakri.service.ChakriService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/chakri")
public class ChakriController {

    private final ChakriService chakriService;
    private final QnAEntryRepository qnaEntryRepository;

    public ChakriController(ChakriService chakriService,
                            QnAEntryRepository qnaEntryRepository) {

        this.chakriService = chakriService;
        this.qnaEntryRepository = qnaEntryRepository;
    }

    @PostMapping(value = "/jd-and-cv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public JSONData<UUID> postJDAndCV(@RequestPart("jd") MultipartFile jd,
                                      @RequestPart("cv") MultipartFile cv) {

        chakriService.validateJdAndCv(jd, cv);

        QnAEntry qnaEntry = chakriService.createNewQnAEntry(jd, cv);
        qnaEntry = qnaEntryRepository.saveOrUpdate(qnaEntry);

        chakriService.runBackgroundQuestionJob(qnaEntry);

        return JSONData.create("id", qnaEntry.getId());
    }

    @GetMapping("/question/{id}")
    public QuestionDto getQuestion(@PathVariable("id") UUID id) {
        return chakriService.getQuestionDto(id);
    }

    @PostMapping("/answer/{id}")
    public ResponseEntity<?> postAnswer(@PathVariable("id") UUID id,
                                        @RequestBody AnswerDto answerDto) {
        chakriService.processAnswer(id, answerDto);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/evaluation-score/{id}")
    public EvaluationScoreDto getEvaluationScore(@PathVariable("id") UUID id) {
        return chakriService.getEvaluationScoreDto(id);
    }
}