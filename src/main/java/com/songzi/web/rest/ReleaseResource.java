package com.songzi.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.songzi.domain.Release;
import com.songzi.service.ReleaseService;
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
 * REST controller for managing Release.
 */
@RestController
@RequestMapping("/api")
public class ReleaseResource {

    private final Logger log = LoggerFactory.getLogger(ReleaseResource.class);

    private static final String ENTITY_NAME = "release";

    private final ReleaseService releaseService;

    public ReleaseResource(ReleaseService releaseService) {
        this.releaseService = releaseService;
    }

    /**
     * POST  /releases : Create a new release.
     *
     * @param release the release to create
     * @return the ResponseEntity with status 201 (Created) and with body the new release, or with status 400 (Bad Request) if the release has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/releases")
    @Timed
    public ResponseEntity<Release> createRelease(@RequestBody Release release) throws URISyntaxException {
        log.debug("REST request to save Release : {}", release);
        if (release.getId() != null) {
            throw new BadRequestAlertException("A new release cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Release result = releaseService.save(release);
        return ResponseEntity.created(new URI("/api/releases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /releases : Updates an existing release.
     *
     * @param release the release to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated release,
     * or with status 400 (Bad Request) if the release is not valid,
     * or with status 500 (Internal Server Error) if the release couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/releases")
    @Timed
    public ResponseEntity<Release> updateRelease(@RequestBody Release release) throws URISyntaxException {
        log.debug("REST request to update Release : {}", release);
        if (release.getId() == null) {
            return createRelease(release);
        }
        Release result = releaseService.save(release);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, release.getId().toString()))
            .body(result);
    }

    /**
     * GET  /releases : get all the releases.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of releases in body
     */
    @GetMapping("/releases")
    @Timed
    public List<Release> getAllReleases() {
        log.debug("REST request to get all Releases");
        return releaseService.findAll();
        }

    /**
     * GET  /releases/:id : get the "id" release.
     *
     * @param id the id of the release to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the release, or with status 404 (Not Found)
     */
    @GetMapping("/releases/{id}")
    @Timed
    public ResponseEntity<Release> getRelease(@PathVariable Long id) {
        log.debug("REST request to get Release : {}", id);
        Release release = releaseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(release));
    }

    /**
     * DELETE  /releases/:id : delete the "id" release.
     *
     * @param id the id of the release to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/releases/{id}")
    @Timed
    public ResponseEntity<Void> deleteRelease(@PathVariable Long id) {
        log.debug("REST request to delete Release : {}", id);
        releaseService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
