package com.songzi.service;

import com.songzi.domain.CheckItemAnswer;
import com.songzi.domain.User;
import com.songzi.repository.CheckItemAnswerRepository;
import com.songzi.service.dto.CheckItemAnswerDTO;
import com.songzi.web.rest.vm.CheckItemAnswerVM;
import liquibase.util.StringUtils;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing CheckItemAnswer.
 */
@Service
@Transactional
public class CheckItemAnswerService {

    private final Logger log = LoggerFactory.getLogger(CheckItemAnswerService.class);

    private final CheckItemAnswerRepository checkItemAnswerRepository;

    @Autowired private UserService userService;

    public CheckItemAnswerService(CheckItemAnswerRepository checkItemAnswerRepository) {
        this.checkItemAnswerRepository = checkItemAnswerRepository;
    }

    /**
     * Save a checkItemAnswer.
     *
     * @param checkItemAnswer the entity to save
     * @return the persisted entity
     */
    public CheckItemAnswer save(CheckItemAnswer checkItemAnswer) {
        log.debug("Request to save CheckItemAnswer : {}", checkItemAnswer);
        return checkItemAnswerRepository.save(checkItemAnswer);
    }

    /**
     * Get all the checkItemAnswers.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<CheckItemAnswer> findAll() {
        log.debug("Request to get all CheckItemAnswers");
        return checkItemAnswerRepository.findAll();
    }

    /**
     * Get one checkItemAnswer by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public CheckItemAnswer findOne(Long id) {
        log.debug("Request to get CheckItemAnswer : {}", id);
        return checkItemAnswerRepository.findOne(id);
    }

    /**
     * Delete the checkItemAnswer by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CheckItemAnswer : {}", id);
        checkItemAnswerRepository.delete(id);
    }

    /**
     * @param login
     * @return
     */
    public List<CheckItemAnswerDTO> findAllByUser(String login) {
        if (StringUtils.isNotEmpty(login)){
            User user = userService.getUserWithAuthoritiesByLogin(login).get();

            checkItemAnswerRepository.findAll();
        }
        return null;
    }
}
