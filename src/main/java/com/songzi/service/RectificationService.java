package com.songzi.service;

import com.songzi.domain.Rectification;
import com.songzi.repository.RectificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
/**
 * Service Implementation for managing Rectification.
 */
@Service
@Transactional
public class RectificationService {

    private final Logger log = LoggerFactory.getLogger(RectificationService.class);

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
        log.debug("Request to save Rectification : {}", rectification);        return rectificationRepository.save(rectification);
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
    public Optional<Rectification> findOne(Long id) {
        log.debug("Request to get Rectification : {}", id);
        return rectificationRepository.findById(id);
    }

    /**
     * Delete the rectification by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Rectification : {}", id);
        rectificationRepository.deleteById(id);
    }
}
