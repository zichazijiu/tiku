package com.songzi.service;

import com.songzi.domain.*;
import com.songzi.domain.enumeration.DeleteFlag;
import com.songzi.domain.enumeration.ExamineStatus;
import com.songzi.repository.*;
import com.songzi.web.rest.vm.QuestionVM;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AnswerMonthOutScheduledTask implements Runnable {

    private SubjectRepository subjectRepository;

    private ProjectRepository projectRepository;

    private ExamineRepository examineRepository;

    private ExaminerRepository examinerRepository;

    private DepartmentRepository departmentRepository;

    private ExamineSubjectService examineSubjectService;

    public AnswerMonthOutScheduledTask(ExaminerRepository examinerRepository, SubjectRepository subjectRepository, ExamineRepository examineRepository,ProjectRepository projectRepository,DepartmentRepository departmentRepository,ExamineSubjectService examineSubjectService){
        this.examinerRepository = examinerRepository;
        this.subjectRepository = subjectRepository;
        this.examineRepository = examineRepository;
        this.projectRepository = projectRepository;
        this.departmentRepository = departmentRepository;
        this.examineSubjectService = examineSubjectService;
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
        examine.setName(department.getName()+"-"+examiner.getName()+ project.getName() + "-" +"的考评");
        examine.setScore(0f);
        examine.setDuration(project.getDuration());
        examine.setStatus(ExamineStatus.FINISHED);
        examine.setResult("{}");
        LocalDateTime localDateTime = LocalDateTime.now().minusMonths(1);
        //答题时间在当前时间往前推一个月  保证答题和项目都在一个月中
        examine.setCreatedDate(localDateTime.toInstant(ZoneOffset.UTC));
        examine.setActualDuration(examine.getDuration());

        Long userId = examine.getUserId();
        int times = examiner.getTime();
        examiner.setTime(times + 1);

        List<Subject> subjectMap = subjectRepository.findAllByProjectId(project.getId());
        for(Subject subject : subjectMap){
            examineSubjectService.doExamineSubjectWrongCount(subject.getId(),examine.getDepartmentId());
        }
        examinerRepository.save(examiner);

        examine =  examineRepository.save(examine);
    }
}
