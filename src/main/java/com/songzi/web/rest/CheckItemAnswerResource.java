package com.songzi.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.songzi.domain.CheckItemAnswer;
import com.songzi.domain.CheckItemAnswer_;
import com.songzi.service.CheckItemAnswerService;
import com.songzi.service.dto.CheckItemAnswerDTO;
import com.songzi.web.rest.errors.BadRequestAlertException;
import com.songzi.web.rest.util.HeaderUtil;
import com.songzi.web.rest.vm.CheckItemAnswerVM;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing CheckItemAnswer.
 */
@RestController
@RequestMapping("/api")
public class CheckItemAnswerResource {

    private final Logger log = LoggerFactory.getLogger(CheckItemAnswerResource.class);

    private static final String ENTITY_NAME = "checkItemAnswer";

    private final CheckItemAnswerService checkItemAnswerService;

    public CheckItemAnswerResource(CheckItemAnswerService checkItemAnswerService) {
        this.checkItemAnswerService = checkItemAnswerService;
    }

    /**
     * POST  /check-item-answers : Create a new checkItemAnswer.
     *
     * @param checkItemAnswer the checkItemAnswer to create
     * @return the ResponseEntity with status 201 (Created) and with body the new checkItemAnswer, or with status 400 (Bad Request) if the checkItemAnswer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/check-item-answers")
    @Timed
    public ResponseEntity<CheckItemAnswer> createCheckItemAnswer(@RequestBody CheckItemAnswer checkItemAnswer) throws URISyntaxException {
        log.debug("REST request to save CheckItemAnswer : {}", checkItemAnswer);
        if (checkItemAnswer.getId() != null) {
            throw new BadRequestAlertException("A new checkItemAnswer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CheckItemAnswer result = checkItemAnswerService.save(checkItemAnswer);
        return ResponseEntity.created(new URI("/api/check-item-answers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /check-item-answers : Updates an existing checkItemAnswer.
     *
     * @param checkItemAnswer the checkItemAnswer to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated checkItemAnswer,
     * or with status 400 (Bad Request) if the checkItemAnswer is not valid,
     * or with status 500 (Internal Server Error) if the checkItemAnswer couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/check-item-answers")
    @Timed
    public ResponseEntity<CheckItemAnswer> updateCheckItemAnswer(@RequestBody CheckItemAnswer checkItemAnswer) throws URISyntaxException {
        log.debug("REST request to update CheckItemAnswer : {}", checkItemAnswer);
        if (checkItemAnswer.getId() == null) {
            return createCheckItemAnswer(checkItemAnswer);
        }
        CheckItemAnswer result = checkItemAnswerService.save(checkItemAnswer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, checkItemAnswer.getId().toString()))
            .body(result);
    }

    /**
     * GET  /check-item-answers : get all the checkItemAnswers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of checkItemAnswers in body
     */
    @GetMapping("/check-item-answers")
    @Timed
    public List<CheckItemAnswer> getAllCheckItemAnswers() {
        log.debug("REST request to get all CheckItemAnswers");
        return checkItemAnswerService.findAll();
        }

    /**
     * GET  /check-item-answers/:id : get the "id" checkItemAnswer.
     *
     * @param id the id of the checkItemAnswer to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the checkItemAnswer, or with status 404 (Not Found)
     */
    @GetMapping("/check-item-answers/{id}")
    @Timed
    public ResponseEntity<CheckItemAnswer> getCheckItemAnswer(@PathVariable Long id) {
        log.debug("REST request to get CheckItemAnswer : {}", id);
        CheckItemAnswer checkItemAnswer = checkItemAnswerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(checkItemAnswer));
    }

    /**
     * DELETE  /check-item-answers/:id : delete the "id" checkItemAnswer.
     *
     * @param id the id of the checkItemAnswer to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/check-item-answers/{id}")
    @Timed
    public ResponseEntity<Void> deleteCheckItemAnswer(@PathVariable Long id) {
        log.debug("REST request to delete CheckItemAnswer : {}", id);
        checkItemAnswerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/user/check-item-detail")
    @Timed
    @ApiOperation("用户查询提报详情")
    public List<CheckItemAnswerDTO> getUserCheckItemAnswer(@RequestParam String login) {
        log.debug("REST request to get userCheckItemAnswer : {}", login);
        return checkItemAnswerService.findAllByUser(login);
    }

}
