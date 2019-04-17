package com.songzi.service;

import com.songzi.domain.ReleaseHistory;
import com.songzi.repository.ReleaseHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing ReleaseHistory.
 */
@Service
@Transactional
public class ReleaseHistoryService {

    private final Logger log = LoggerFactory.getLogger(ReleaseHistoryService.class);

    private final ReleaseHistoryRepository releaseHistoryRepository;

    public ReleaseHistoryService(ReleaseHistoryRepository releaseHistoryRepository) {
        this.releaseHistoryRepository = releaseHistoryRepository;
    }

    /**
     * Save a releaseHistory.
     *
     * @param releaseHistory the entity to save
     * @return the persisted entity
     */
    public ReleaseHistory save(ReleaseHistory releaseHistory) {
        log.debug("Request to save ReleaseHistory : {}", releaseHistory);
        return releaseHistoryRepository.save(releaseHistory);
    }

    /**
     * Get all the releaseHistories.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ReleaseHistory> findAll() {
        log.debug("Request to get all ReleaseHistories");
        return releaseHistoryRepository.findAll();
    }

    /**
     * Get one releaseHistory by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ReleaseHistory findOne(Long id) {
        log.debug("Request to get ReleaseHistory : {}", id);
        return releaseHistoryRepository.findOne(id);
    }

    /**
     * Delete the releaseHistory by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ReleaseHistory : {}", id);
        releaseHistoryRepository.delete(id);
    }
}
