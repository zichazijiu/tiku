package com.songzi.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.songzi.domain.LogBackup;

import com.songzi.repository.LogBackupRepository;
import com.songzi.service.LogBackupSerivce;
import com.songzi.service.dto.LogBackupDTO;
import com.songzi.web.rest.errors.BadRequestAlertException;
import com.songzi.web.rest.util.HeaderUtil;
import com.songzi.web.rest.util.PaginationUtil;
import com.songzi.web.rest.vm.DataBackupQueryVM;
import com.songzi.web.rest.vm.LogBackupQueryVM;
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
 * REST controller for managing LogBackup.
 */
@RestController
@RequestMapping("/api")
public class LogBackupResource {

    private final Logger log = LoggerFactory.getLogger(LogBackupResource.class);

    private static final String ENTITY_NAME = "logBackup";

    private final LogBackupRepository logBackupRepository;

    @Autowired
    private LogBackupSerivce logBackupSerivce;

    public LogBackupResource(LogBackupRepository logBackupRepository) {
        this.logBackupRepository = logBackupRepository;
    }

    /**
     * POST  /log-backups : Create a new logBackup.
     *
     * @param logBackup the logBackup to create
     * @return the ResponseEntity with status 201 (Created) and with body the new logBackup, or with status 400 (Bad Request) if the logBackup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect

    @PostMapping("/log-backups")
    @Timed
    public ResponseEntity<LogBackup> createLogBackup(@Valid @RequestBody LogBackup logBackup) throws URISyntaxException {
        log.debug("REST request to save LogBackup : {}", logBackup);
        if (logBackup.getId() != null) {
            throw new BadRequestAlertException("A new logBackup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LogBackup result = logBackupRepository.save(logBackup);
        return ResponseEntity.created(new URI("/api/log-backups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }*/

    /**
     * PUT  /log-backups : Updates an existing logBackup.
     *
     * @param logBackup the logBackup to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated logBackup,
     * or with status 400 (Bad Request) if the logBackup is not valid,
     * or with status 500 (Internal Server Error) if the logBackup couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect

    @PutMapping("/log-backups")
    @Timed
    public ResponseEntity<LogBackup> updateLogBackup(@Valid @RequestBody LogBackup logBackup) throws URISyntaxException {
        log.debug("REST request to update LogBackup : {}", logBackup);
        if (logBackup.getId() == null) {
            return createLogBackup(logBackup);
        }
        LogBackup result = logBackupRepository.save(logBackup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, logBackup.getId().toString()))
            .body(result);
    }
     */
    /**
     * GET  /log-backups : get all the logBackups.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of logBackups in body
     */
    @GetMapping("/log-backups")
    @Timed
    @ApiOperation(value = "获取日志列表")
    public ResponseEntity<List<LogBackupDTO>> getAllLogBackups(LogBackupQueryVM logBackupQueryVM,Pageable pageable) {
        log.debug("获取日志列表");
        Page<LogBackupDTO> page = logBackupSerivce.getAllLogBackups(logBackupQueryVM,pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/log-backups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/iemport-backups")
    @Timed
    @ApiOperation(value = "导入导出列表")
    public ResponseEntity<List<LogBackupDTO>> getAllIEmportBackups(DataBackupQueryVM dataBackupQueryVM,Pageable pageable) {
        log.debug("导入导出列表");
        Page<LogBackupDTO> page = logBackupSerivce.getAllIEmportBackups(dataBackupQueryVM,pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/log-backups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/database-backups")
    @Timed
    @ApiOperation(value = "数据备份列表")
    public ResponseEntity<List<LogBackupDTO>> getAllDatabaseBackups(DataBackupQueryVM dataBackupQueryVM, Pageable pageable) {
        log.debug("数据备份列表");
        Page<LogBackupDTO> page = logBackupSerivce.getAllDatabaseBackups(dataBackupQueryVM,pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/log-backups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /log-backups/:id : get the "id" logBackup.
     *
     * @param id the id of the logBackup to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the logBackup, or with status 404 (Not Found)

    @GetMapping("/log-backups/{id}")
    @Timed
    public ResponseEntity<LogBackup> getLogBackup(@PathVariable Long id) {
        log.debug("REST request to get LogBackup : {}", id);
        LogBackup logBackup = logBackupRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(logBackup));
    } */

    /**
     * DELETE  /log-backups/:id : delete the "id" logBackup.
     *
     * @param id the id of the logBackup to delete
     * @return the ResponseEntity with status 200 (OK)

    @DeleteMapping("/log-backups/{id}")
    @Timed
    public ResponseEntity<Void> deleteLogBackup(@PathVariable Long id) {
        log.debug("REST request to delete LogBackup : {}", id);
        logBackupRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }*/
}
