package com.rahathossain.chakri.service;

import com.rahathossain.chakri.repository.QnAEntryRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ChakriScheduler {

    private final QnAEntryRepository qnaEntryRepository;

    public ChakriScheduler(QnAEntryRepository qnaEntryRepository){
        this.qnaEntryRepository = qnaEntryRepository;
    }

    @Scheduled(cron = "0 0 18 * * ?")
    public void eraseExpiredQnAEntries(){
        qnaEntryRepository.deleteAllCreatedBefore(new Date());
    }
}
