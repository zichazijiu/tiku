package com.songzi.service;

import com.songzi.domain.Rectification;
import com.songzi.domain.RemainsQuestion;
import com.songzi.repository.RectificationRepository;
import com.songzi.repository.RemainsQuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing Rectification.
 */
@Service
@Transactional
public class RectificationService {

    private final Logger log = LoggerFactory.getLogger(RectificationService.class);

    @Autowired private RemainsQuestionRepository remainsQuestionRepository;

    private final RectificationRepository rectificationRepository;

    public RectificationService(RectificationRepository rectificationRepository) {
        this.rectificationRepository = rectificationRepository;
    }

    /**
     * Save a rectification.
     *
     * @param rectification the entity to save
     * @return the persisted entity
     */
    public Rectification save(Rectification rectification) {
        log.debug("Request to save Rectification : {}", rectification);
        return rectificationRepository.save(rectification);
    }

    /**
     * Get all the rectifications.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Rectification> findAll() {
        log.debug("Request to get all Rectifications");
        return rectificationRepository.findAll();
    }

    /**
     * Get one rectification by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Rectification findOne(Long id) {
        log.debug("Request to get Rectification : {}", id);
        return rectificationRepository.findOne(id);
    }

    /**
     * Delete the rectification by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Rectification : {}", id);
        rectificationRepository.delete(id);
    }

    /**
     * 保存整改信息
     * @param remainsQuestionId
     * @param rectification
     * @return
     */
    public Rectification save(Long remainsQuestionId, Rectification rectification) {
        log.debug("保存ID是{}的遗留问题的整改信息：{}", remainsQuestionId, rectification);
        // 整改信息
        Rectification obj = rectificationRepository.save(rectification);
        // 遗留问题
        RemainsQuestion remainsQuestion = remainsQuestionRepository.findOne(remainsQuestionId);
        // 建立遗留问题和整改信息的关系
        remainsQuestion.setRectification(obj);
        // 更新
        remainsQuestionRepository.save(remainsQuestion);

        return obj;
    }
}
