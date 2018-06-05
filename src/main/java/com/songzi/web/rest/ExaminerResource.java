package com.songzi.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.songzi.domain.Examiner;

import com.songzi.repository.ExaminerRepository;
import com.songzi.web.rest.errors.BadRequestAlertException;
import com.songzi.web.rest.util.HeaderUtil;
import com.songzi.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Examiner.
 */
@RestController
@RequestMapping("/api")
public class ExaminerResource {

    private final Logger log = LoggerFactory.getLogger(ExaminerResource.class);

    private static final String ENTITY_NAME = "examiner";

    private final ExaminerRepository examinerRepository;

    public ExaminerResource(ExaminerRepository examinerRepository) {
        this.examinerRepository = examinerRepository;
    }

    /**
     * POST  /examiners : Create a new examiner.
     *
     * @param examiner the examiner to create
     * @return the ResponseEntity with status 201 (Created) and with body the new examiner, or with status 400 (Bad Request) if the examiner has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/examiners")
    @Timed
    public ResponseEntity<Examiner> createExaminer(@Valid @RequestBody Examiner examiner) throws URISyntaxException {
        log.debug("REST request to save Examiner : {}", examiner);
        if (examiner.getId() != null) {
            throw new BadRequestAlertException("A new examiner cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Examiner result = examinerRepository.save(examiner);
        return ResponseEntity.created(new URI("/api/examiners/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /examiners : Updates an existing examiner.
     *
     * @param examiner the examiner to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated examiner,
     * or with status 400 (Bad Request) if the examiner is not valid,
     * or with status 500 (Internal Server Error) if the examiner couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/examiners")
    @Timed
    public ResponseEntity<Examiner> updateExaminer(@Valid @RequestBody Examiner examiner) throws URISyntaxException {
        log.debug("REST request to update Examiner : {}", examiner);
        if (examiner.getId() == null) {
            return createExaminer(examiner);
        }
        Examiner result = examinerRepository.save(examiner);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, examiner.getId().toString()))
            .body(result);
    }

    /**
     * GET  /examiners : get all the examiners.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of examiners in body
     */
    @GetMapping("/examiners")
    @Timed
    public ResponseEntity<List<Examiner>> getAllExaminers(Pageable pageable) {
        log.debug("REST request to get a page of Examiners");
        Page<Examiner> page = examinerRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/examiners");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /examiners/:id : get the "id" examiner.
     *
     * @param id the id of the examiner to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the examiner, or with status 404 (Not Found)
     */
    @GetMapping("/examiners/{id}")
    @Timed
    public ResponseEntity<Examiner> getExaminer(@PathVariable Long id) {
        log.debug("REST request to get Examiner : {}", id);
        Examiner examiner = examinerRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(examiner));
    }

    /**
     * DELETE  /examiners/:id : delete the "id" examiner.
     *
     * @param id the id of the examiner to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/examiners/{id}")
    @Timed
    public ResponseEntity<Void> deleteExaminer(@PathVariable Long id) {
        log.debug("REST request to delete Examiner : {}", id);
        examinerRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
