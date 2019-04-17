package com.songzi.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.songzi.domain.ReleaseHistory;
import com.songzi.service.ReleaseHistoryService;
import com.songzi.web.rest.errors.BadRequestAlertException;
import com.songzi.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ReleaseHistory.
 */
@RestController
@RequestMapping("/api")
public class ReleaseHistoryResource {

    private final Logger log = LoggerFactory.getLogger(ReleaseHistoryResource.class);

    private static final String ENTITY_NAME = "releaseHistory";

    private final ReleaseHistoryService releaseHistoryService;

    public ReleaseHistoryResource(ReleaseHistoryService releaseHistoryService) {
        this.releaseHistoryService = releaseHistoryService;
    }

    /**
     * POST  /release-histories : Create a new releaseHistory.
     *
     * @param releaseHistory the releaseHistory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new releaseHistory, or with status 400 (Bad Request) if the releaseHistory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/release-histories")
    @Timed
    public ResponseEntity<ReleaseHistory> createReleaseHistory(@RequestBody ReleaseHistory releaseHistory) throws URISyntaxException {
        log.debug("REST request to save ReleaseHistory : {}", releaseHistory);
        if (releaseHistory.getId() != null) {
            throw new BadRequestAlertException("A new releaseHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReleaseHistory result = releaseHistoryService.save(releaseHistory);
        return ResponseEntity.created(new URI("/api/release-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /release-histories : Updates an existing releaseHistory.
     *
     * @param releaseHistory the releaseHistory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated releaseHistory,
     * or with status 400 (Bad Request) if the releaseHistory is not valid,
     * or with status 500 (Internal Server Error) if the releaseHistory couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/release-histories")
    @Timed
    public ResponseEntity<ReleaseHistory> updateReleaseHistory(@RequestBody ReleaseHistory releaseHistory) throws URISyntaxException {
        log.debug("REST request to update ReleaseHistory : {}", releaseHistory);
        if (releaseHistory.getId() == null) {
            return createReleaseHistory(releaseHistory);
        }
        ReleaseHistory result = releaseHistoryService.save(releaseHistory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, releaseHistory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /release-histories : get all the releaseHistories.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of releaseHistories in body
     */
    @GetMapping("/release-histories")
    @Timed
    public List<ReleaseHistory> getAllReleaseHistories() {
        log.debug("REST request to get all ReleaseHistories");
        return releaseHistoryService.findAll();
        }

    /**
     * GET  /release-histories/:id : get the "id" releaseHistory.
     *
     * @param id the id of the releaseHistory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the releaseHistory, or with status 404 (Not Found)
     */
    @GetMapping("/release-histories/{id}")
    @Timed
    public ResponseEntity<ReleaseHistory> getReleaseHistory(@PathVariable Long id) {
        log.debug("REST request to get ReleaseHistory : {}", id);
        ReleaseHistory releaseHistory = releaseHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(releaseHistory));
    }

    /**
     * DELETE  /release-histories/:id : delete the "id" releaseHistory.
     *
     * @param id the id of the releaseHistory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/release-histories/{id}")
    @Timed
    public ResponseEntity<Void> deleteReleaseHistory(@PathVariable Long id) {
        log.debug("REST request to delete ReleaseHistory : {}", id);
        releaseHistoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
