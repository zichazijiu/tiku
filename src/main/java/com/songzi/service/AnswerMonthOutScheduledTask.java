package com.songzi.service;

import com.songzi.domain.Department;
import com.songzi.domain.Examine;
import com.songzi.domain.Examiner;
import com.songzi.domain.Project;
import com.songzi.domain.enumeration.DeleteFlag;
import com.songzi.domain.enumeration.ExamineStatus;
import com.songzi.repository.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

public class AnswerMonthOutScheduledTask implements Runnable {

    private SubjectRepository subjectRepository;

    private ProjectRepository projectRepository;

    private ExamineRepository examineRepository;

    private ExaminerRepository examinerRepository;

    private DepartmentRepository departmentRepository;

    public AnswerMonthOutScheduledTask(ExaminerRepository examinerRepository, SubjectRepository subjectRepository, ExamineRepository examineRepository,ProjectRepository projectRepository,DepartmentRepository departmentRepository){
        this.examinerRepository = examinerRepository;
        this.subjectRepository = subjectRepository;
        this.examineRepository = examineRepository;
        this.projectRepository = projectRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public void run() {
        try{
            doTimeOutMethod();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void doTimeOutMethod(){
        List<Examiner> examinerList = examinerRepository.findAll();
        for(Examiner examiner : examinerList){
            List<Long> hadDoneProjectIds = examineRepository.findAllByDelFlagAndUserId(DeleteFlag.NORMAL,examiner.getUserId())
                .stream().map(examine -> {
                    return examine.getProjectId();
                }).collect(Collectors.toList());
            List<Project> notDoneProject = projectRepository.findAllByDelFlagAndIdNotIn(DeleteFlag.NORMAL,hadDoneProjectIds);
            for(Project project : notDoneProject){
                try{
                    doZeroScoreExamin(examiner,project);
                }catch (Exception e){
                    continue;
                }
            }
        }

    }

    //没开始答题的项目  统统生成考试成绩为0的记录
    public void doZeroScoreExamin(Examiner examiner,Project project){
        Examine examine = new Examine();
        examine.setDelFlag(DeleteFlag.NORMAL);
        examine.setUserId(examiner.getUserId());
        examine.setDepartmentId(examiner.getDepartmentId());
        Department department = departmentRepository.findOne(examiner.getDepartmentId());
        examine.setName(department.getName()+"-"+examiner.getName()+ project.getName() + "-" +"的考试");
        examine.setScore(0);
        examine.setDuration(project.getDuration());
        examine.setStatus(ExamineStatus.FINISHED);
        examine.setResult("{}");
        LocalDateTime localDateTime = LocalDateTime.now().minusMonths(1);
        //答题时间在当前时间往前推一个月  保证答题和项目都在一个月中
        examine.setCreatedDate(localDateTime.toInstant(ZoneOffset.UTC));
        examine.setActualDuration(examine.getDuration());
        examine =  examineRepository.save(examine);
    }
}
