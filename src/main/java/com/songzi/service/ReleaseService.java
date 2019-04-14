package com.songzi.service;

import com.songzi.domain.Release;
import com.songzi.repository.ReleaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing Release.
 */
@Service
@Transactional
public class ReleaseService {

    private final Logger log = LoggerFactory.getLogger(ReleaseService.class);

    private final ReleaseRepository releaseRepository;

    public ReleaseService(ReleaseRepository releaseRepository) {
        this.releaseRepository = releaseRepository;
    }

    /**
     * Save a release.
     *
     * @param release the entity to save
     * @return the persisted entity
     */
    public Release save(Release release) {
        log.debug("Request to save Release : {}", release);
        return releaseRepository.save(release);
    }

    /**
     * Get all the releases.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Release> findAll() {
        log.debug("Request to get all Releases");
        return releaseRepository.findAll();
    }

    /**
     * Get one release by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Release findOne(Long id) {
        log.debug("Request to get Release : {}", id);
        return releaseRepository.findOne(id);
    }

    /**
     * Delete the release by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Release : {}", id);
        releaseRepository.delete(id);
    }
}
