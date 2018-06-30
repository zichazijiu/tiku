package com.songzi.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.songzi.domain.Examiner;

import com.songzi.repository.ExaminerRepository;
import com.songzi.security.AuthoritiesConstants;
import com.songzi.security.SecurityUtils;
import com.songzi.service.ExaminerService;
import com.songzi.service.dto.ExaminerDTO;
import com.songzi.web.rest.errors.BadRequestAlertException;
import com.songzi.web.rest.util.HeaderUtil;
import com.songzi.web.rest.util.PaginationUtil;
import com.songzi.web.rest.vm.ExaminerQueryVM;
import com.songzi.web.rest.vm.ExaminerVM;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;
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

    private final ExaminerService examinerService;

    public ExaminerResource(ExaminerRepository examinerRepository,ExaminerService examinerService) {
        this.examinerRepository = examinerRepository;
        this.examinerService = examinerService;
    }

    /**
     * POST  /examiners : Create a new examiner.
     *
     * @param examinerVM the examiner to create
     * @return the ResponseEntity with status 201 (Created) and with body the new examiner, or with status 400 (Bad Request) if the examiner has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/examiners")
    @Timed
    public ResponseEntity<Examiner> createExaminer(@Valid @RequestBody ExaminerVM examinerVM) throws URISyntaxException {
        log.debug("REST request to save Examiner : {}", examinerVM);
        if (examinerVM.getId() != null) {
            throw new BadRequestAlertException("新建考评员ID必须为空", ENTITY_NAME, "ID必须为空");
        }
        Examiner result = new Examiner();//examinerRepository.save(examiner);
        return ResponseEntity.created(new URI("/api/examiners/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /examiners : Updates an existing examiner.
     *
     * @param examinerVM the examiner to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated examiner,
     * or with status 400 (Bad Request) if the examiner is not valid,
     * or with status 500 (Internal Server Error) if the examiner couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/examiners")
    @Timed
    public ResponseEntity<Examiner> updateExaminer(@Valid @RequestBody ExaminerVM examinerVM) throws URISyntaxException {
        log.debug("REST request to update Examiner : {}", examinerVM);
        if (examinerVM.getId() == null) {
            throw new BadRequestAlertException("更新考评员信息ID不能为空", ENTITY_NAME, "ID不能为空");
        }
        Examiner result = new Examiner();//examinerRepository.save(examinerVM);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, examinerVM.getId().toString()))
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
    public ResponseEntity<List<ExaminerDTO>> getAllExaminers(ExaminerQueryVM examinerQueryVM, Pageable pageable) {
        log.debug("REST request to get a page of Examiners {}{}",examinerQueryVM,pageable);
        Page<ExaminerDTO> page = examinerService.getAll(examinerQueryVM,pageable);
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
        Examiner examiner;
        if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)){
            examiner = examinerRepository.findOne(id);
        }else{
            throw new BadRequestAlertException("非管理员用户不访问此接口",this.getClass().getName(),"没有权限");
        }
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(examiner));
    }

    /**
     * GET  /examiners/:id : get the "id" examiner.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the examiner, or with status 404 (Not Found)
     */
    @GetMapping("/examiners/current")
    @Timed
    @ApiOperation(value = "已测试：获取考评员详情")
    public ResponseEntity<ExaminerDTO> getCurrentExaminer() {
        log.debug("REST request to get current Examiner");
        ExaminerDTO examiner = examinerService.getCurrentExaminer();
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
