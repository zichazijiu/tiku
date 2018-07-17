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
    public ResponseEntity<?> getStatisticsSortDepartment(@RequestParam(value = "compareTime") String compareTime) {
        log.debug("REST request to get 单位考评排名");
        List<StatisticsDTO> result = statisticsService.getStatisticsSortDepartment(compareTime);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }

    @GetMapping("/statistics/department")
    @ApiOperation(value = "单位自查月份对比数据")
    @Timed
    public ResponseEntity<?> getStatisticsDepartment(@RequestParam(value = "departmentId") Long departmentId) {
        log.debug("REST request to get 单位自查月份对比数据");
        List<StatisticsDTO> result = statisticsService.getStatisticsDepartment(departmentId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }

    @GetMapping("/statistics/weakness")
    @ApiOperation(value = "薄弱环节分析")
    @Timed
    public ResponseEntity<?> getStatisticsWeakness(@RequestParam(value = "compareTime") String compareTime) {
        log.debug("REST request to get 薄弱环节分析");
        //随便造点数据
        WeaknessDTO weaknessDTO = new WeaknessDTO();

        ProblemPercentDTO problemPercentDTO = new ProblemPercentDTO();
        problemPercentDTO.setTotalRight(100);
        List<SubjectPercentDTO> subjectPercentDTOS = new ArrayList<>();
        SubjectPercentDTO subjectPercentDTO = new SubjectPercentDTO();
        subjectPercentDTO.setId(1L);
        subjectPercentDTO.setCount(40);
        SubjectPercentDTO subjectPercentDTO1 = new SubjectPercentDTO();
        subjectPercentDTO1.setId(2L);
        subjectPercentDTO1.setCount(30);
        SubjectPercentDTO subjectPercentDTO2 = new SubjectPercentDTO();
        subjectPercentDTO2.setId(3L);
        subjectPercentDTO2.setCount(30);

        subjectPercentDTOS.add(subjectPercentDTO);
        subjectPercentDTOS.add(subjectPercentDTO1);
        subjectPercentDTOS.add(subjectPercentDTO2);
        problemPercentDTO.setSubjectPercentDTOList(subjectPercentDTOS);
        weaknessDTO.setProblemPercentDTO(problemPercentDTO);

        AnswerTimePercentDTO answerTimePercentDTO = new AnswerTimePercentDTO();
        answerTimePercentDTO.setTotalMinutes(100);
        List<TimePercentDTO> timePercentDTOS = new ArrayList<>();
        TimePercentDTO timePercentDTO = new TimePercentDTO();
        timePercentDTO.setKey("0-30");
        timePercentDTO.setPercent("60");

        TimePercentDTO timePercentDTO1 = new TimePercentDTO();
        timePercentDTO1.setKey("30-");
        timePercentDTO1.setPercent("40");

        timePercentDTOS.add(timePercentDTO);
        timePercentDTOS.add((timePercentDTO1));
        answerTimePercentDTO.setTimePercentDTOList(timePercentDTOS);

        weaknessDTO.setAnswerTimePercentDTO(answerTimePercentDTO);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(weaknessDTO));
    }


}
