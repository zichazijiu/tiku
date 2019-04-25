package com.songzi.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.songzi.domain.RemainsQuestion;
import com.songzi.domain.ReportItems;
import com.songzi.service.RemainsQuestionService;
import com.songzi.service.ReportItemsService;
import com.songzi.web.rest.errors.BadRequestAlertException;
import com.songzi.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for managing RemainsQuestion.
 */
@RestController
@RequestMapping("/api")
public class RemainsQuestionResource {

    private final Logger log = LoggerFactory.getLogger(RemainsQuestionResource.class);

    @Autowired
    private ReportItemsService reportItemsService;

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
            return createRemainsQuestion(remainsQuestion);
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
        RemainsQuestion remainsQuestion = remainsQuestionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(remainsQuestion));
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

    @PutMapping("/remains-questions/save")
    @Timed
    @ApiOperation("保存遗留问题")
    public ResponseEntity<RemainsQuestion> saveRemainsQuestion(@Valid @RequestParam Long reportItemId,
                                                               @RequestBody RemainsQuestion remainsQuestion)
        throws URISyntaxException {

        ReportItems reportItems = reportItemsService.findOne(reportItemId);
        if (reportItems == null) {
            log.error("没有找到ID是{}的提报项目", reportItemId);
            return ResponseEntity.notFound().build();
        }
        remainsQuestion.setReportItems(reportItems);
        return updateRemainsQuestion(remainsQuestion);
    }

    /**
     * Problem distribution statistics by organization
     *
     * @param departmentId
     * @return
     */
    @GetMapping("/remains-questions/getCountByDepartmentId/{departmentId}")
    @Timed
    @ApiOperation("问题分布统计(按组织机构)")
    public List<Map<String, Object>> countByDepartmentId(@PathVariable Long departmentId) {
        log.debug("REST request to count RemainsQuestion by departmentId : {}", departmentId);
        return remainsQuestionService.countByDepartmentId(departmentId);
    }

    /**
     * Problem distribution statistics by self evaluation
     *
     * @param checkItemId
     * @return
     */
    @GetMapping("/remains-questions/getCountByCheckItemId/{checkItemId}")
    @Timed
    @ApiOperation("问题分布统计(按自评项)")
    public List<Map<String, Object>> getCountByCheckItemId(@PathVariable Long checkItemId) {
        log.debug("REST request to count RemainsQuestion by checkItemId : {}", checkItemId);
        return remainsQuestionService.countByCheckItemId(checkItemId);
    }

    /**
     * Rectification analysis
     *
     * @param checkItemId
     * @return
     */
    @GetMapping("/remains-questions/getCountRectification/{checkItemId}")
    @Timed
    @ApiOperation("整改分析")
    public ResponseEntity<List<Map<String, Object>>> getCountRectification(@PathVariable Long checkItemId) {
        log.debug("Request to count Rectification by checkItemId : {}", checkItemId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(remainsQuestionService.countRectification(checkItemId)));
    }

}
