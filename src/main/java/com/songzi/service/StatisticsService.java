package com.songzi.service;

import com.songzi.domain.Department;
import com.songzi.domain.ExamineSubject;
import com.songzi.repository.DepartmentRepository;
import com.songzi.repository.ExamineRepository;
import com.songzi.repository.ExamineSubjectRepository;
import com.songzi.repository.StatisticsRepository;
import com.songzi.service.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public List<StatisticsDTO> getStatisticsCurrentUser(){
        Long userId = userService.getCurrentUserId();
        List<StatisticsDTO> statisticsDTOList = statisticsRepository.getStatisticsCurrentUser(userId).stream().map(objects -> {
            StatisticsDTO statisticsDTO = new StatisticsDTO();
            statisticsDTO.setScore(Integer.parseInt(objects[0]+""));
            statisticsDTO.setMonth(objects[1]+"");
            return statisticsDTO;
        }).collect(Collectors.toList());
        return statisticsDTOList;
    }


    public List<StatisticsDTO> getStatisticsDepartment(Long departmentId){
        String year = LocalDate.now().getYear()+"";
        List<StatisticsDTO> statisticsDTOList = examineSubjectRepository.findAllByYearGroupByMonthOrderByWrongTimeDesc(year,departmentId).stream().map(objects -> {
            StatisticsDTO statisticsDTO = new StatisticsDTO();
            statisticsDTO.setScore(Integer.parseInt(objects[0]+""));
            statisticsDTO.setMonth(objects[1]+"");
            return statisticsDTO;
        }).collect(Collectors.toList());
        return statisticsDTOList;
    }


    public List<StatisticsDTO> getStatisticsSortDepartment(String compareTime){
        String[] yearMonthArray = compareTime.split("-");
        String year = yearMonthArray[0];
        String month = yearMonthArray[1];
        List<StatisticsDTO> statisticsDTOList = examineSubjectRepository.findAllByYearAndMonthGroupByDepartmentOrderByWrongTimeDesc(year,month).stream().map(objects -> {
            StatisticsDTO statisticsDTO = new StatisticsDTO();
            statisticsDTO.setScore(Integer.parseInt(objects[0]+""));
            Department department = departmentRepository.findOne(Long.parseLong(objects[1]+""));
            if(department != null){
                statisticsDTO.setDepartmentName(department.getName());
            }
            return statisticsDTO;
        }).collect(Collectors.toList());
        return statisticsDTOList;
    }


    public ProblemPercentDTO getProblemPercentDTO(String yearMonth,Long departmentId){
        Pageable pageable = new PageRequest(0,10);
        String[] yearMonthArray = yearMonth.split("-");
        String year = yearMonthArray[0];
        String month = yearMonthArray[1];
        ProblemPercentDTO problemPercentDTO = new ProblemPercentDTO();
        int total = 0;
        List<SubjectPercentDTO> subjectPercentDTOList;
        if(departmentId != null && departmentId != 0){
            total = examineSubjectRepository.countWrongTimeByYearAndMonthAndDepartmentId(year,month,departmentId);
            subjectPercentDTOList = examineSubjectRepository.findAllByYearAndMonthAndDepartmentIdOrderByWrongTimeDesc(year,month,departmentId,pageable).stream()
                .map(examineSubject -> {
                    SubjectPercentDTO subjectPercentDTO = new SubjectPercentDTO();
                    subjectPercentDTO.setId(examineSubject.getSubjectId());
                    subjectPercentDTO.setCount(examineSubject.getWrongTime());
                    return subjectPercentDTO;
                }).collect(Collectors.toList());
        }else{
            total = examineSubjectRepository.countWrongTimeByYearAndMonth(year,month);
            subjectPercentDTOList = examineSubjectRepository.findAllByYearAndMonthOrderByWrongTimeDesc(year,month,pageable).stream()
                .map(objects -> {
                    SubjectPercentDTO subjectPercentDTO = new SubjectPercentDTO();
                    subjectPercentDTO = new SubjectPercentDTO();
                    subjectPercentDTO.setId((Long) objects[1]);
                    subjectPercentDTO.setCount(Integer.parseInt(objects[0]+""));
                    return subjectPercentDTO;
                }).collect(Collectors.toList());
        }
        problemPercentDTO.setTotalRight(total);

        problemPercentDTO.setSubjectPercentDTOList(subjectPercentDTOList);
        return problemPercentDTO;
    }

    public AnswerTimePercentDTO getAnswerTimePercentDTO(String yearMonth,Long departmentId){
        String[] yearMonthArray = yearMonth.split("-");
        String year = yearMonthArray[0];
        String month = yearMonthArray[1];
        AnswerTimePercentDTO answerTimePercentDTO = new AnswerTimePercentDTO();
        List<TimePercentDTO> timePercentDTOList = new ArrayList<>();
        int totalS = 0;
        if(departmentId != null && departmentId != 0){
            totalS = examineSubjectRepository.getTotalCountTimsByDepartmentId(year,month,departmentId);
            int finalTotalS = totalS;
            List<TimePercentDTO> timePercentDTOS = examineSubjectRepository.findAllByYearAndMonthAndDepartmentIdOrderByWrongTimeDesc(year,month,departmentId).stream()
                .map(examineSubject -> {
                    TimePercentDTO timePercentDTO = new TimePercentDTO();
                    timePercentDTO.setKey(examineSubject.getSubjectId()+"");
                    timePercentDTO.setPercent(examineSubject.getWrongTime() +"");
                    return timePercentDTO;
                }).collect(Collectors.toList());
            timePercentDTOList = timePercentDTOS;
        }else{
            totalS = examineSubjectRepository.getTotalCountTims(year,month);
            timePercentDTOList = examineSubjectRepository.findAllByYearAndMonthOrderByWrongTimeDesc(year,month).stream()
                .map(objects -> {
                    TimePercentDTO timePercentDTO = new TimePercentDTO();
                    timePercentDTO.setKey(objects[1]+"");
                    timePercentDTO.setPercent(objects[0]+"");
                    return timePercentDTO;
                }).collect(Collectors.toList());
        }

        //处理成百分比的
        int wrongPercent = 0;
        for(TimePercentDTO timePercentDTO : timePercentDTOList){
            int times = Integer.parseInt(timePercentDTO.getPercent());
            int percent = times * 100 /totalS;
            wrongPercent = wrongPercent + percent;
            timePercentDTO.setPercent(percent+"");
        }

        TimePercentDTO rightPercent = new TimePercentDTO();
        rightPercent.setKey("else");
        rightPercent.setPercent((100-wrongPercent)+"");

        timePercentDTOList.add(rightPercent);

        answerTimePercentDTO.setTimePercentDTOList(timePercentDTOList);
        answerTimePercentDTO.setTotalMinutes(totalS);
        return answerTimePercentDTO;
    }

}
