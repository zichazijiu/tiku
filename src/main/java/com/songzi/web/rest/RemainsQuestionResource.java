package com.songzi.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.songzi.domain.RemainsQuestion;
import com.songzi.service.RemainsQuestionService;
import com.songzi.web.rest.errors.BadRequestAlertException;
import com.songzi.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing RemainsQuestion.
 */
@RestController
@RequestMapping("/api")
public class RemainsQuestionResource {

    private final Logger log = LoggerFactory.getLogger(RemainsQuestionResource.class);

    private static final String ENTITY_NAME = "remainsQuestion";

    private final RemainsQuestionService remainsQuestionService;

    public RemainsQuestionResource(RemainsQuestionService remainsQuestionService) {
        this.remainsQuestionService = remainsQuestionService;
    }

    /**
     * POST  /remains-questions : Create a new remainsQuestion.
     *
     * @param remainsQuestion the remainsQuestion to create
     * @return the ResponseEntity with status 201 (Created) and with body the new remainsQuestion, or with status 400 (Bad Request) if the remainsQuestion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/remains-questions")
    @Timed
    public ResponseEntity<RemainsQuestion> createRemainsQuestion(@Valid @RequestBody RemainsQuestion remainsQuestion) throws URISyntaxException {
        log.debug("REST request to save RemainsQuestion : {}", remainsQuestion);
        if (remainsQuestion.getId() != null) {
            throw new BadRequestAlertException("A new remainsQuestion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RemainsQuestion result = remainsQuestionService.save(remainsQuestion);
        return ResponseEntity.created(new URI("/api/remains-questions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /remains-questions : Updates an existing remainsQuestion.
     *
     * @param remainsQuestion the remainsQuestion to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated remainsQuestion,
     * or with status 400 (Bad Request) if the remainsQuestion is not valid,
     * or with status 500 (Internal Server Error) if the remainsQuestion couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/remains-questions")
    @Timed
    public ResponseEntity<RemainsQuestion> updateRemainsQuestion(@Valid @RequestBody RemainsQuestion remainsQuestion) throws URISyntaxException {
        log.debug("REST request to update RemainsQuestion : {}", remainsQuestion);
        if (remainsQuestion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RemainsQuestion result = remainsQuestionService.save(remainsQuestion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, remainsQuestion.getId().toString()))
            .body(result);
    }

    /**
     * GET  /remains-questions : get all the remainsQuestions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of remainsQuestions in body
     */
    @GetMapping("/remains-questions")
    @Timed
    public List<RemainsQuestion> getAllRemainsQuestions() {
        log.debug("REST request to get all RemainsQuestions");
        return remainsQuestionService.findAll();
    }

    /**
     * GET  /remains-questions/:id : get the "id" remainsQuestion.
     *
     * @param id the id of the remainsQuestion to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the remainsQuestion, or with status 404 (Not Found)
     */
    @GetMapping("/remains-questions/{id}")
    @Timed
    public ResponseEntity<RemainsQuestion> getRemainsQuestion(@PathVariable Long id) {
        log.debug("REST request to get RemainsQuestion : {}", id);
        Optional<RemainsQuestion> remainsQuestion = remainsQuestionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(remainsQuestion);
    }

    /**
     * DELETE  /remains-questions/:id : delete the "id" remainsQuestion.
     *
     * @param id the id of the remainsQuestion to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/remains-questions/{id}")
    @Timed
    public ResponseEntity<Void> deleteRemainsQuestion(@PathVariable Long id) {
        log.debug("REST request to delete RemainsQuestion : {}", id);
        remainsQuestionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
