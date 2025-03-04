package com.rahathossain.chakri.repository;

import com.rahathossain.chakri.exception.RecordNotFoundException;
import com.rahathossain.chakri.model.QnAEntry;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Repository
public class QnAEntryRepository {

    private final EntityManager em;

    public QnAEntryRepository(EntityManager em) {
        this.em = em;
    }

    public QnAEntry get(UUID id) {
        try {
            return em.find(QnAEntry.class, id);

        } catch (Exception e) {
            throw new RecordNotFoundException("Not found");
        }
    }

    @Transactional
    public QnAEntry saveOrUpdate(QnAEntry qnaEntry) {

        if (Objects.isNull(qnaEntry.getId())) {
            em.persist(qnaEntry);

        } else {
            qnaEntry = em.merge(qnaEntry);
        }

        return qnaEntry;
    }

    @Transactional
    public void deleteAllCreatedBefore(Date date) {
        em.createQuery("DELETE FROM QnAEntry qnaEntry WHERE qnaEntry.createdAt <= :createdAt")
                .setParameter(":createdAt", date).executeUpdate();
    }
}
