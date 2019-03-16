package com.songzi.service;

import com.songzi.domain.RemainsQuestion;
import com.songzi.repository.RemainsQuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing RemainsQuestion.
 */
@Service
@Transactional
public class RemainsQuestionService {

    private final Logger log = LoggerFactory.getLogger(RemainsQuestionService.class);

    private final RemainsQuestionRepository remainsQuestionRepository;

    public RemainsQuestionService(RemainsQuestionRepository remainsQuestionRepository) {
        this.remainsQuestionRepository = remainsQuestionRepository;
    }

    /**
     * Save a remainsQuestion.
     *
     * @param remainsQuestion the entity to save
     * @return the persisted entity
     */
    public RemainsQuestion save(RemainsQuestion remainsQuestion) {
        log.debug("Request to save RemainsQuestion : {}", remainsQuestion);
        return remainsQuestionRepository.save(remainsQuestion);
    }

    /**
     * Get all the remainsQuestions.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<RemainsQuestion> findAll() {
        log.debug("Request to get all RemainsQuestions");
        return remainsQuestionRepository.findAll();
    }

    /**
     * Get one remainsQuestion by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public RemainsQuestion findOne(Long id) {
        log.debug("Request to get RemainsQuestion : {}", id);
        return remainsQuestionRepository.findOne(id);
    }

    /**
     * Delete the remainsQuestion by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete RemainsQuestion : {}", id);
        remainsQuestionRepository.delete(id);
    }
}
