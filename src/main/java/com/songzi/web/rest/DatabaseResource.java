package com.songzi.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.songzi.service.DatabaseService;
import com.songzi.service.dto.DepartmentDTO;
import com.songzi.web.rest.util.PaginationUtil;
import com.songzi.web.rest.vm.DepartmentQueryVM;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(("/api"))
public class DatabaseResource {

    private final Logger log = LoggerFactory.getLogger(DatabaseResource.class);

    @Autowired
    private DatabaseService databaseService;
    @GetMapping("/databasebackup")
    @Timed
    @ApiOperation(value = "备份数据库")
    public ResponseEntity<?> backupDatabase() throws IOException, InterruptedException {
        log.debug("备份数据库");
        Map map = databaseService.doDatabaseBackup();
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(map));
    }

}
