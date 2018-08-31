package com.songzi.web.rest;

import com.codahale.metrics.annotation.Timed;

import com.songzi.service.StatisticsService;
import com.songzi.service.dto.*;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
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
        log.debug("REST request to get 个人自查分析");
        List<StatisticsDTO> result = statisticsService.getStatisticsCurrentUser();
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }


    @GetMapping("/statistics/sortdepartment")
    @ApiOperation(value = "单位考评排名")
    @Timed
    public ResponseEntity<?> getStatisticsSortDepartment(@RequestParam(value = "type", required = false) String type,
                                                         @RequestParam(value = "order", required = false) String order,
                                                         @RequestParam(value = "compareTime") String compareTime) {
        log.debug("REST request to get 单位考评排名");
        List<StatisticsDTO> result;
        if ("wrong-times".equals(type))
            result = statisticsService.getStatisticsSortDepartment(compareTime);
        else
            result = statisticsService.getStatisticsSortDepartmentAVGScore(compareTime, order);


        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }

    @GetMapping("/statistics/department")
    @ApiOperation(value = "单位自查月份对比数据")
    @Timed
    public ResponseEntity<?> getStatisticsDepartment(@RequestParam(value = "departmentId") Long departmentId) {
        log.debug("REST request to get 单位自查月份对比数据");
        // List<StatisticsDTO> result = statisticsService.getStatisticsDepartment(departmentId);
        List<StatisticsDTO> result = statisticsService.getStatisticsAVGScoreWith6MonthByDepartment(departmentId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }

    @GetMapping("/statistics/weakness")
    @ApiOperation(value = "薄弱环节分析")
    @Timed
    public ResponseEntity<?> getStatisticsWeakness(@RequestParam(value = "compareTime") String compareTime, @RequestParam(value = "departmentId", required = false) Long departmentId) {
        log.debug("REST request to get 薄弱环节分析");
        WeaknessDTO weaknessDTO = new WeaknessDTO();
        // 薄弱问题排行
        weaknessDTO.setProblemPercentDTO(statisticsService.getProblemPercentDTO(compareTime, departmentId));
        // 薄弱问题占比
        weaknessDTO.setAnswerTimePercentDTO(statisticsService.getAnswerTimePercentDTO(compareTime, departmentId));
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(weaknessDTO));
    }


}
