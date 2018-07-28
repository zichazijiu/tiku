package com.songzi.service;

import com.songzi.domain.Department;
import com.songzi.repository.DepartmentRepository;
import com.songzi.repository.ExamineRepository;
import com.songzi.repository.ExamineSubjectRepository;
import com.songzi.repository.StatisticsRepository;
import com.songzi.service.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
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
        List<StatisticsDTO> statisticsDTOList = statisticsRepository.getStatisticsDepartment(departmentId).stream().map(objects -> {
            StatisticsDTO statisticsDTO = new StatisticsDTO();
            statisticsDTO.setScore(Integer.parseInt(objects[0]+""));
            statisticsDTO.setMonth(objects[1]+"");
            return statisticsDTO;
        }).collect(Collectors.toList());
        return statisticsDTOList;
    }


    public List<StatisticsDTO> getStatisticsSortDepartment(String compareTime){
        List<StatisticsDTO> statisticsDTOList = statisticsRepository.getStatisticsSortDepartment(compareTime).stream().map(objects -> {
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


    public ProblemPercentDTO getProblemPercentDTO(String yearMonth){
        String[] yearMonthArray = yearMonth.split("-");
        String year = yearMonthArray[0];
        String month = yearMonthArray[1];
        ProblemPercentDTO problemPercentDTO = new ProblemPercentDTO();
        int total = examineSubjectRepository.countAllByYearAndMonth(year,month);
        problemPercentDTO.setTotalRight(total);
        List<SubjectPercentDTO> subjectPercentDTOList = examineSubjectRepository.findAllByYearAndMonth(year,month).stream()
            .map(examineSubject -> {
                SubjectPercentDTO subjectPercentDTO = new SubjectPercentDTO();
                subjectPercentDTO.setId(examineSubject.getSubjectId());
                subjectPercentDTO.setCount(examineSubject.getRightTime());
                return subjectPercentDTO;
            }).collect(Collectors.toList());
        problemPercentDTO.setSubjectPercentDTOList(subjectPercentDTOList);
        return problemPercentDTO;
    }

    public AnswerTimePercentDTO getAnswerTimePercentDTO(String yearMonth){
        AnswerTimePercentDTO answerTimePercentDTO = new AnswerTimePercentDTO();
        List<TimePercentDTO> timePercentDTOList = new ArrayList<>();
        Long totalS = examineRepository.getTotalTime(yearMonth);
        if(totalS == null){
            answerTimePercentDTO.setTotalMinutes(0);
            answerTimePercentDTO.setTimePercentDTOList(timePercentDTOList);
            TimePercentDTO timePercentDTO30 = new TimePercentDTO();
            timePercentDTO30.setKey("0-30");
            timePercentDTO30.setPercent("100");
            timePercentDTOList.add(timePercentDTO30);

            TimePercentDTO timePercentDTO30_50 = new TimePercentDTO();
            timePercentDTO30_50.setKey("30-50");
            timePercentDTO30_50.setPercent("0");
            timePercentDTOList.add(timePercentDTO30_50);

            TimePercentDTO timePercentDTO50_ = new TimePercentDTO();
            timePercentDTO50_.setKey("50-");
            timePercentDTO50_.setPercent("0");
            timePercentDTOList.add(timePercentDTO50_);

            return answerTimePercentDTO;
        }
        int totalM = Integer.parseInt(totalS/60+"");
        answerTimePercentDTO.setTotalMinutes(totalM);
        answerTimePercentDTO.setTimePercentDTOList(timePercentDTOList);

        Long duration30 = examineRepository.getTotalTime30(yearMonth);
        if(duration30 == null){
            duration30 = 0L;
        }
        TimePercentDTO timePercentDTO30 = new TimePercentDTO();
        timePercentDTO30.setKey("0-30");
        timePercentDTO30.setPercent(Integer.parseInt(duration30*100/totalS +"") +"");
        timePercentDTOList.add(timePercentDTO30);

        Long duration30_50 = examineRepository.getTotalTime30_50(yearMonth);
        if(duration30_50 == null){
            duration30_50 = 0L;
        }
        TimePercentDTO timePercentDTO30_50 = new TimePercentDTO();
        timePercentDTO30_50.setKey("30-50");
        timePercentDTO30_50.setPercent(Integer.parseInt(duration30_50*100/totalS +"") +"");
        timePercentDTOList.add(timePercentDTO30_50);

        Long duration50_ = examineRepository.getTotalTime50_(yearMonth);
        if(duration50_ == null){
            duration50_ = 0L;
        }
        TimePercentDTO timePercentDTO50_ = new TimePercentDTO();
        timePercentDTO50_.setKey("50-");
        timePercentDTO50_.setPercent(Integer.parseInt(duration50_*100/totalS +"") +"");
        timePercentDTOList.add(timePercentDTO50_);
        return answerTimePercentDTO;
    }

}
