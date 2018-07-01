package com.songzi.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.songzi.domain.Examine;
import com.songzi.repository.ExamineRepository;
import com.songzi.service.ExamineService;
import com.songzi.service.dto.ExamineDTO;
import com.songzi.web.rest.errors.BadRequestAlertException;
import com.songzi.web.rest.util.HeaderUtil;
import com.songzi.web.rest.util.PaginationUtil;
import com.songzi.web.rest.vm.ExamineVM;
import com.songzi.web.rest.vm.QuestionVM;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 * REST controller for managing Examine.
 */
@RestController
@RequestMapping("/api")
public class ExamineResource {

    private final Logger log = LoggerFactory.getLogger(ExamineResource.class);

    private static final String ENTITY_NAME = "examine";

    private final ExamineRepository examineRepository;

    @Autowired
    private ExamineService examineService;

    public ExamineResource(ExamineRepository examineRepository) {
        this.examineRepository = examineRepository;
    }

    /**
     * POST  /examines : Create a new examine.
     *
     * @param examineVM the examine to create
     * @return the ResponseEntity with status 201 (Created) and with body the new examine, or with status 400 (Bad Request) if the examine has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/examines")
    @Timed
    @ApiOperation(value = "开始答题")
    public ResponseEntity<ExamineDTO> createExamine(@Valid @RequestBody ExamineVM examineVM) throws URISyntaxException {
        log.debug("REST request to save Examine : {}", examineVM);
        if (examineVM.getId() != null) {
            throw new BadRequestAlertException("新建考试ID必须为空", ENTITY_NAME, "ID必须为空");
        }
        ExamineDTO result = examineService.insert(examineVM);
        return ResponseEntity.created(new URI("/api/examines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /examines : Updates an existing examine.
     *
     * @param examineVM the examine to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated examine,
     * or with status 400 (Bad Request) if the examine is not valid,
     * or with status 500 (Internal Server Error) if the examine couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect

    @PutMapping("/examines")
    @Timed
    public ResponseEntity<Examine> updateExamine(@Valid @RequestBody ExamineVM examineVM) throws URISyntaxException {
        log.debug("REST request to update Examine : {}", examineVM);
        if (examineVM.getId() == null) {
            throw new BadRequestAlertException("更新考试ID不能为空", ENTITY_NAME, "ID不能为空");
        }
        Examine result = examineService.update(examineVM);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }*/

    /**
     * GET  /examines : get all the examines.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of examines in body
     */
    @GetMapping("/examines")
    @Timed
    @ApiOperation(value = "查询所有的考试")
    public ResponseEntity<List<ExamineDTO>> getAllExamines(Pageable pageable) {
        log.debug("REST request to get a page of Examines");
        Page<ExamineDTO> page = examineService.getAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/examines");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /examines/:id : get the "id" examine.
     *
     * @param id the id of the examine to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the examine, or with status 404 (Not Found)
     */
    @GetMapping("/examines/{id}")
    @Timed
    @ApiOperation(value = "通过id查询考试详情")
    public ResponseEntity<ExamineDTO> getExamine(@PathVariable Long id) {
        log.debug("REST request to get Examine : {}", id);
        ExamineDTO examineDTO = examineService.getOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(examineDTO));
    }

    /**
     * DELETE  /examines/:id : delete the "id" examine.
     *
     * @param id the id of the examine to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/examines/{id}")
    @Timed
    public ResponseEntity<Void> deleteExamine(@PathVariable Long id) {
        log.debug("REST request to delete Examine : {}", id);
        examineRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @PutMapping("/examines/{examineId}/questions")
    @Timed
    @ApiOperation(value = "答题")
    public ResponseEntity answer(@PathVariable(value = "examineId") Long examineId, @RequestBody List<QuestionVM> questionVMList){
        examineService.answer(examineId,questionVMList);
        return null;
    }
}
