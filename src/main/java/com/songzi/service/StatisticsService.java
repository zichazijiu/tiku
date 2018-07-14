package com.songzi.service;

import com.songzi.domain.Department;
import com.songzi.repository.DepartmentRepository;
import com.songzi.repository.StatisticsRepository;
import com.songzi.service.dto.StatisticsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

}
