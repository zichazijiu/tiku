package com.songzi.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.songzi.domain.Rectification;
import com.songzi.domain.RemainsQuestion;
import com.songzi.service.RectificationService;
import com.songzi.service.RemainsQuestionService;
import com.songzi.web.rest.errors.BadRequestAlertException;
import com.songzi.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Rectification.
 */
@RestController
@RequestMapping("/api")
public class RectificationResource {

    private final Logger log = LoggerFactory.getLogger(RectificationResource.class);

    @Autowired
    private RemainsQuestionService remainsQuestionService;

    private static final String ENTITY_NAME = "rectification";

    private final RectificationService rectificationService;

    public RectificationResource(RectificationService rectificationService) {
        this.rectificationService = rectificationService;
    }

    /**
     * POST  /rectifications : Create a new rectification.
     *
     * @param rectification the rectification to create
     * @return the ResponseEntity with status 201 (Created) and with body the new rectification, or with status 400 (Bad Request) if the rectification has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rectifications")
    @Timed
    public ResponseEntity<Rectification> createRectification(@Valid @RequestBody Rectification rectification) throws URISyntaxException {
        log.debug("REST request to save Rectification : {}", rectification);
        if (rectification.getId() != null) {
            throw new BadRequestAlertException("A new rectification cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Rectification result = rectificationService.save(rectification);
        return ResponseEntity.created(new URI("/api/rectifications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rectifications : Updates an existing rectification.
     *
     * @param rectification the rectification to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated rectification,
     * or with status 400 (Bad Request) if the rectification is not valid,
     * or with status 500 (Internal Server Error) if the rectification couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rectifications")
    @Timed
    public ResponseEntity<Rectification> updateRectification(@Valid @RequestBody Rectification rectification) throws URISyntaxException {
        log.debug("REST request to update Rectification : {}", rectification);
        if (rectification.getId() == null) {
            return createRectification(rectification);
        }
        Rectification result = rectificationService.save(rectification);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, rectification.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rectifications : get all the rectifications.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of rectifications in body
     */
    @GetMapping("/rectifications")
    @Timed
    public List<Rectification> getAllRectifications() {
        log.debug("REST request to get all Rectifications");
        return rectificationService.findAll();
    }

    /**
     * GET  /rectifications/:id : get the "id" rectification.
     *
     * @param id the id of the rectification to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the rectification, or with status 404 (Not Found)
     */
    @GetMapping("/rectifications/{id}")
    @Timed
    public ResponseEntity<Rectification> getRectification(@PathVariable Long id) {
        log.debug("REST request to get Rectification : {}", id);
        Rectification rectification = rectificationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rectification));
    }

    /**
     * DELETE  /rectifications/:id : delete the "id" rectification.
     *
     * @param id the id of the rectification to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rectifications/{id}")
    @Timed
    public ResponseEntity<Void> deleteRectification(@PathVariable Long id) {
        log.debug("REST request to delete Rectification : {}", id);
        rectificationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @PutMapping("/rectifications/save")
    @Timed
    @ApiOperation("保留整改信息")
    public ResponseEntity<Rectification> saveRectification(@Valid @RequestParam Long remainsQuestionId,
                                                           @RequestBody Rectification rectification)
        throws URISyntaxException {
        RemainsQuestion remainsQuestion = remainsQuestionService.findOne(remainsQuestionId);
        if (remainsQuestion == null) {
            log.error("没有找到ID是{}的遗留问题", remainsQuestionId);
            return ResponseEntity.notFound().build();
        }
        rectification.setRemainsQuestion(remainsQuestion);
        return updateRectification(rectification);

    }
}
