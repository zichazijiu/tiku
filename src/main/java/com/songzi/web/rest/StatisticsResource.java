package com.songzi.web.rest;

import com.codahale.metrics.annotation.Timed;

import com.songzi.service.StatisticsService;
import com.songzi.service.dto.StatisticsDTO;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Statistics.
 */
@RestController
@RequestMapping("/api")
public class StatisticsResource {

    private final Logger log = LoggerFactory.getLogger(StatisticsResource.class);

    private static final String ENTITY_NAME = "statistics";

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/statistics/examiner")
    @ApiOperation(value = "个人自查分析")
    @Timed
    public ResponseEntity<?> getStatisticsCurrentUser() {
        log.debug("REST request to get a page of Projects for current user");
        List<StatisticsDTO> result = statisticsService.getStatisticsCurrentUser();
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }


    @GetMapping("/statistics/sortdepartment")
    @ApiOperation(value = "单位考评排名")
    @Timed
    public ResponseEntity<?> getStatisticsSortDepartment(@RequestParam(value = "compareTime") String compareTime) {
        log.debug("REST request to get a page of Projects for current user");
        List<StatisticsDTO> result = statisticsService.getStatisticsSortDepartment(compareTime);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }

    @GetMapping("/statistics/department")
    @ApiOperation(value = "单位自查月份对比数据")
    @Timed
    public ResponseEntity<?> getStatisticsDepartment(@RequestParam(value = "departmentId") Long departmentId) {
        log.debug("REST request to get a page of Projects for current user");
        List<StatisticsDTO> result = statisticsService.getStatisticsDepartment(departmentId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }


}
