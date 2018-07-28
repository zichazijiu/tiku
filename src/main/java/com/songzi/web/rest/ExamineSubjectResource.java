package com.songzi.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.songzi.domain.ExamineSubject;

import com.songzi.repository.ExamineSubjectRepository;
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
 * REST controller for managing ExamineSubject.
 */
@RestController
@RequestMapping("/api")
public class ExamineSubjectResource {

    private final Logger log = LoggerFactory.getLogger(ExamineSubjectResource.class);

    private static final String ENTITY_NAME = "examineSubject";

    private final ExamineSubjectRepository examineSubjectRepository;

    public ExamineSubjectResource(ExamineSubjectRepository examineSubjectRepository) {
        this.examineSubjectRepository = examineSubjectRepository;
    }

    /**
     * POST  /examine-subjects : Create a new examineSubject.
     *
     * @param examineSubject the examineSubject to create
     * @return the ResponseEntity with status 201 (Created) and with body the new examineSubject, or with status 400 (Bad Request) if the examineSubject has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/examine-subjects")
    @Timed
    public ResponseEntity<ExamineSubject> createExamineSubject(@RequestBody ExamineSubject examineSubject) throws URISyntaxException {
        log.debug("REST request to save ExamineSubject : {}", examineSubject);
        if (examineSubject.getId() != null) {
            throw new BadRequestAlertException("A new examineSubject cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExamineSubject result = examineSubjectRepository.save(examineSubject);
        return ResponseEntity.created(new URI("/api/examine-subjects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /examine-subjects : Updates an existing examineSubject.
     *
     * @param examineSubject the examineSubject to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated examineSubject,
     * or with status 400 (Bad Request) if the examineSubject is not valid,
     * or with status 500 (Internal Server Error) if the examineSubject couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/examine-subjects")
    @Timed
    public ResponseEntity<ExamineSubject> updateExamineSubject(@RequestBody ExamineSubject examineSubject) throws URISyntaxException {
        log.debug("REST request to update ExamineSubject : {}", examineSubject);
        if (examineSubject.getId() == null) {
            return createExamineSubject(examineSubject);
        }
        ExamineSubject result = examineSubjectRepository.save(examineSubject);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, examineSubject.getId().toString()))
            .body(result);
    }

    /**
     * GET  /examine-subjects : get all the examineSubjects.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of examineSubjects in body
     */
    @GetMapping("/examine-subjects")
    @Timed
    public List<ExamineSubject> getAllExamineSubjects() {
        log.debug("REST request to get all ExamineSubjects");
        return examineSubjectRepository.findAll();
        }

    /**
     * GET  /examine-subjects/:id : get the "id" examineSubject.
     *
     * @param id the id of the examineSubject to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the examineSubject, or with status 404 (Not Found)
     */
    @GetMapping("/examine-subjects/{id}")
    @Timed
    public ResponseEntity<ExamineSubject> getExamineSubject(@PathVariable Long id) {
        log.debug("REST request to get ExamineSubject : {}", id);
        ExamineSubject examineSubject = examineSubjectRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(examineSubject));
    }

    /**
     * DELETE  /examine-subjects/:id : delete the "id" examineSubject.
     *
     * @param id the id of the examineSubject to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/examine-subjects/{id}")
    @Timed
    public ResponseEntity<Void> deleteExamineSubject(@PathVariable Long id) {
        log.debug("REST request to delete ExamineSubject : {}", id);
        examineSubjectRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
