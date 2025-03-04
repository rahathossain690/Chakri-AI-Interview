package com.rahathossain.chakri.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QnAEntry {

    @ElementCollection
    @CollectionTable(name = "qna_entry_question", joinColumns = @JoinColumn(name = "qna_entry_id"))
    List<Question> questions;

    @ElementCollection
    @CollectionTable(name = "qna_entry_answer", joinColumns = @JoinColumn(name = "qna_entry_id"))
    List<Answer> answers;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @NotEmpty
    @Column(columnDefinition = "TEXT")
    private String jdContent;

    @NotNull
    @NotEmpty
    @Column(columnDefinition = "TEXT")
    private String cvContent;

    @Column(columnDefinition = "TEXT")
    private String qnaJsonStr;

    @Embedded
    private EvaluationScore evaluationScore;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @PastOrPresent
    private Date createdAt;
}
