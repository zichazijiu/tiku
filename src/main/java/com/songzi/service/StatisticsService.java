package com.songzi.service;

import com.songzi.domain.Department;
import com.songzi.domain.ExamineSubject;
import com.songzi.domain.enumeration.DeleteFlag;
import com.songzi.repository.DepartmentRepository;
import com.songzi.repository.ExamineRepository;
import com.songzi.repository.ExamineSubjectRepository;
import com.songzi.repository.StatisticsRepository;
import com.songzi.service.dto.*;
import liquibase.configuration.SystemPropertyProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.util.SystemPropertyUtils;

import javax.transaction.Transactional;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class StatisticsService {

    @Autowired
    private StatisticsRepository statisticsRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ExamineSubjectRepository examineSubjectRepository;

    @Autowired
    private ExamineRepository examineRepository;

    public List<StatisticsDTO> getStatisticsCurrentUser() {
        Long userId = userService.getCurrentUserId();
        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        List<StatisticsDTO> statisticsDTOList = statisticsRepository
            .getStatisticsCurrentUser(userId, year).stream().map(objects -> {
                StatisticsDTO statisticsDTO = new StatisticsDTO();
                // 月
                String month = ((String) objects[0]).substring(5);
                statisticsDTO.setMonth(month);
                // 分数
                String grades = (String) objects[1];
                if (StringUtils.isEmpty(grades)) grades = "0";
                Integer score = (int) Float.parseFloat(grades);
                statisticsDTO.setScore(score);

                return statisticsDTO;
            }).collect(Collectors.toList());
        return statisticsDTOList;
    }


    public List<StatisticsDTO> getStatisticsDepartment(Long departmentId) {
        String year = LocalDate.now().getYear() + "";
        List<StatisticsDTO> statisticsDTOList = examineSubjectRepository.findAllByYearGroupByMonthOrderByWrongTimeDesc(year, departmentId).stream().map(objects -> {
            StatisticsDTO statisticsDTO = new StatisticsDTO();
            statisticsDTO.setScore(Integer.parseInt(objects[0] + ""));
            statisticsDTO.setMonth(objects[1] + "");
            return statisticsDTO;
        }).collect(Collectors.toList());
        return statisticsDTOList;
    }

    /**
     * 统计指定部门近六个月参与项目的的平均分
     *
     * @param departmentId
     * @return
     */
    public List<StatisticsDTO> getStatisticsAVGScoreWith6MonthByDepartment(Long departmentId) {
        LocalDate localDate = LocalDate.now();
        String endDate = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        String startDate = localDate.minusMonths(6).format(DateTimeFormatter.ofPattern("yyyy-MM"));
        List<StatisticsDTO> statisticsDTOS = statisticsRepository
            .statistics6MonthAVGScoreByDepartmentAndCreatedDate(departmentId, startDate, endDate)
            .stream()
            .map(objects -> {
                StatisticsDTO statisticsDTO = new StatisticsDTO();
                // 时间
                String date = (String) objects[0];
                statisticsDTO.setMonth(date);
                // 平均分
                String avgScoreStr = (String) objects[1];
                if (StringUtils.isEmpty(avgScoreStr))
                    avgScoreStr = "0";
                int avgScore = (int) Float.parseFloat(avgScoreStr);
                statisticsDTO.setScore(avgScore);

                return statisticsDTO;
            }).collect(Collectors.toList());

        return statisticsDTOS;
    }

    /**
     * 分数记录的是错误次数
     *
     * @param compareTime
     * @return
     */
    public List<StatisticsDTO> getStatisticsSortDepartment(String compareTime) {
        String[] yearMonthArray = compareTime.split("-");
        String year = yearMonthArray[0];
        String month = yearMonthArray[1];
        List<StatisticsDTO> statisticsDTOList = examineSubjectRepository
            .findAllByYearAndMonthGroupByDepartmentOrderByWrongTimeDesc(year, month)
            .stream().map(objects -> {
                StatisticsDTO statisticsDTO = new StatisticsDTO();
                String score = (String) objects[0];
                if (StringUtils.isEmpty(score))
                    score = "0";
                statisticsDTO.setScore(Integer.parseInt( score));
                Department department = departmentRepository.findOne(Long.parseLong(objects[1] + ""));
                if (department != null) {
                    statisticsDTO.setDepartmentName(department.getName());
                }
                return statisticsDTO;
            }).collect(Collectors.toList());
        return statisticsDTOList;
    }

    /**
     * 统计部门的平均分，分数记录的是平均分
     *
     * @param compareTime
     * @return
     */
    public List<StatisticsDTO> getStatisticsSortDepartmentAVGScore(String compareTime, String order) {
        String[] yearMonthArray = compareTime.split("-");
        String year = yearMonthArray[0];
        String month = yearMonthArray[1];
        List<Object[]> objectList;
        if (!StringUtils.isEmpty(order) && "ASC".equals(order.toUpperCase())) {
            objectList = statisticsRepository.statisticsDepartmentAVGScoreTop10ByCreatedDateAndDelFlagOrderByASC(compareTime);
        } else {
            objectList = statisticsRepository.statisticsDepartmentAVGScoreTop10ByCreatedDateAndDelFlagOrderByDESC(compareTime);
        }
        List<StatisticsDTO> statisticsDTOList = objectList
            .stream().map(objects -> {
                StatisticsDTO statisticsDTO = new StatisticsDTO();
                String departmentName = (String) objects[0]; // 部门名称
                statisticsDTO.setDepartmentName(departmentName);
                String score = ((String) objects[1]); // 部门平均分数
                if (StringUtils.isEmpty(score)) score = "0";
                statisticsDTO.setScore((int) Float.parseFloat(score));

                return statisticsDTO;
            }).collect(Collectors.toList());

        return statisticsDTOList;
    }


    public ProblemPercentDTO getProblemPercentDTO(String yearMonth, Long departmentId) {
        Pageable pageable = new PageRequest(0, 10);
        String[] yearMonthArray = yearMonth.split("-");
        String year = yearMonthArray[0];
        String month = yearMonthArray[1];
        ProblemPercentDTO problemPercentDTO = new ProblemPercentDTO();
        int total = 0;
        List<SubjectPercentDTO> subjectPercentDTOList;
        if (departmentId != null && departmentId != 0) {
            total = examineSubjectRepository.countWrongTimeByYearAndMonthAndDepartmentId(year, month, departmentId);
            subjectPercentDTOList = examineSubjectRepository.findAllByYearAndMonthAndDepartmentIdOrderByWrongTimeDesc(year, month, departmentId, pageable)
                .stream().map(examineSubject -> {
                    SubjectPercentDTO subjectPercentDTO = new SubjectPercentDTO();
                    subjectPercentDTO.setId(examineSubject.getSubjectId());
                    subjectPercentDTO.setCount(examineSubject.getWrongTime());
                    return subjectPercentDTO;
                }).collect(Collectors.toList());
        } else {
            total = examineSubjectRepository.countWrongTimeByYearAndMonth(year, month);
            subjectPercentDTOList = examineSubjectRepository.findAllByYearAndMonthOrderByWrongTimeDesc(year, month, pageable).stream()
                .map(objects -> {
                    SubjectPercentDTO subjectPercentDTO = new SubjectPercentDTO();
                    // 错误数
                    subjectPercentDTO.setCount(Integer.parseInt(String.valueOf(objects[0])));
                    // 项目ID
                    subjectPercentDTO.setId((Long) objects[1]);
                    return subjectPercentDTO;
                }).collect(Collectors.toList());
        }
        problemPercentDTO.setTotalRight(total);

        problemPercentDTO.setSubjectPercentDTOList(subjectPercentDTOList);
        return problemPercentDTO;
    }

    public AnswerTimePercentDTO getAnswerTimePercentDTO(String yearMonth, Long departmentId) {
        String[] yearMonthArray = yearMonth.split("-");
        String year = yearMonthArray[0];
        String month = yearMonthArray[1];
        AnswerTimePercentDTO answerTimePercentDTO = new AnswerTimePercentDTO();
        List<TimePercentDTO> timePercentDTOList = new ArrayList<>();
        int totalS = 0;
        if (departmentId != null && departmentId != 0) {
            totalS = examineSubjectRepository.getTotalCountTimsByDepartmentId(year, month, departmentId);
            int finalTotalS = totalS;
            List<TimePercentDTO> timePercentDTOS = examineSubjectRepository.findAllByYearAndMonthAndDepartmentIdOrderByWrongTimeDesc(year, month, departmentId).stream()
                .map(examineSubject -> {
                    TimePercentDTO timePercentDTO = new TimePercentDTO();
                    timePercentDTO.setKey(examineSubject.getSubjectId() + "");
                    timePercentDTO.setPercent(examineSubject.getWrongTime() + "");
                    return timePercentDTO;
                }).collect(Collectors.toList());
            timePercentDTOList = timePercentDTOS;
        } else {
            totalS = examineSubjectRepository.getTotalCountTims(year, month);
            timePercentDTOList = examineSubjectRepository.findAllByYearAndMonthOrderByWrongTimeDesc(year, month).stream()
                .map(objects -> {
                    TimePercentDTO timePercentDTO = new TimePercentDTO();
                    timePercentDTO.setKey(objects[1] + "");
                    timePercentDTO.setPercent(objects[0] + "");
                    return timePercentDTO;
                }).collect(Collectors.toList());
        }

        //处理成百分比的
        int wrongPercent = 0;
        for (TimePercentDTO timePercentDTO : timePercentDTOList) {
            int times = Integer.parseInt(timePercentDTO.getPercent());
            int percent = times * 100 / totalS;
            wrongPercent = wrongPercent + percent;
            timePercentDTO.setPercent(percent + "");
        }

        TimePercentDTO rightPercent = new TimePercentDTO();
        rightPercent.setKey("else");
        rightPercent.setPercent((100 - wrongPercent) + "");

        timePercentDTOList.add(rightPercent);

        answerTimePercentDTO.setTimePercentDTOList(timePercentDTOList);
        answerTimePercentDTO.setTotalMinutes(totalS);
        return answerTimePercentDTO;
    }

}
