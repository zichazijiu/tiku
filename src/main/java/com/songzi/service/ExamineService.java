package com.songzi.service;

import com.alibaba.fastjson.JSONObject;
import com.songzi.domain.*;
import com.songzi.domain.enumeration.DeleteFlag;
import com.songzi.domain.enumeration.ExamineStatus;
import com.songzi.repository.*;
import com.songzi.service.dto.ExamineDTO;
import com.songzi.service.mapper.ExamineMapper;
import com.songzi.service.mapper.ExamineSubjectVMMapper;
import com.songzi.service.mapper.ExamineVMMapper;
import com.songzi.service.mapper.ProjectMapper;
import com.songzi.web.rest.errors.BadRequestAlertException;
import com.songzi.web.rest.vm.ExamineVM;
import com.songzi.web.rest.vm.QuestionVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class ExamineService {

    @Autowired
    private ExamineMapper examineMapper;

    @Autowired
    private ExamineVMMapper examineVMMapper;

    @Autowired
    private ExamineRepository examineRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ExaminerRepository examinerRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private ExamineSubjectVMMapper examineSubjectVMMapper;

    @Autowired
    private LogBackupService logBackupService;

    @Autowired
    private ExamineSubjectService examineSubjectService;

    /**
     *
     * @param examineVM
     * @return
     */
    public ExamineDTO insert(ExamineVM examineVM){
        Long userId = userService.getCurrentUserId();
        List<Examine> examineOld = examineRepository.findAllByProjectIdAndDelFlagAndUserId(examineVM.getProjectId(),DeleteFlag.NORMAL,userId);
        if(examineOld != null && examineOld.size() > 0){
            throw new BadRequestAlertException("该项目已经有正在答题的考试，同一项目不允许有多场考试",this.getClass().getName(),"不允许有多场考试");
        }
        Examine examine = examineVMMapper.toEntity(examineVM);
        examine.setDelFlag(DeleteFlag.NORMAL);
        examine.setUserId(userService.getCurrentUserId());
        Examiner examiner = examinerRepository.findOneByUserId(userService.getCurrentUserId());
        examine.setDepartmentId(examiner.getDepartmentId());
        Department department = departmentRepository.findOne(examiner.getDepartmentId());
        Project project = projectRepository.findOne(examineVM.getProjectId());
        examine.setName(department.getName()+"-"+examiner.getName()+ project.getName() + "-" +"的考试");
        examine.setScore(null);
        examine.setDuration(project.getDuration());
        examine.setStatus(ExamineStatus.NORMAL);
        examine =  examineRepository.save(examine);

        ExamineDTO examineDTO = examineMapper.toDto(examine);
        examineDTO.setProject(projectMapper.toDto(project));
        examineDTO.setExamineSubjectDTOList(
            subjectRepository.findAllByProjectId(project.getId())
                .stream().map(examineSubjectVMMapper :: toDto)
                .collect(Collectors.toList()));

        return examineDTO;
    }

    /**
     *
     * @param examineVM
     * @return
     */
    public Examine update(ExamineVM examineVM){
        Examine examine = examineRepository.findOne(examineVM.getId());
        examine.setProjectId(examineVM.getProjectId());
        return examineRepository.save(examine);
    }

    /**
     *
     * @param pageable
     * @return
     */
    public Page<ExamineDTO> getAll(Pageable pageable){
        return examineRepository.findAllByDelFlag(DeleteFlag.NORMAL,pageable)
            .map(examineMapper :: toDto);
    }

    public ExamineDTO getOne(Long id){
        Examine examine = examineRepository.findOne(id);
        Examiner examiner = examinerRepository.findOneByUserId(userService.getCurrentUserId());
        Department department = departmentRepository.findOne(examiner.getDepartmentId());
        Project project = projectRepository.findOne(examine.getProjectId());

        ExamineDTO examineDTO = examineMapper.toDto(examine);
        examineDTO.setProject(projectMapper.toDto(project));
        examineDTO.setExamineSubjectDTOList(
            subjectRepository.findAllByProjectId(project.getId())
                .stream().map(examineSubjectVMMapper :: toDto)
                .collect(Collectors.toList()));
        return examineDTO;
    }

    public ExamineDTO answer(Long examineId,String submit,List<QuestionVM> questionVMList){
        Examine examine = examineRepository.findOne(examineId);
        if(examine.getStatus() == ExamineStatus.FINISHED){
            throw new BadRequestAlertException("该场考试已结束，不能再次提交答案",this.getClass().getName(),"考试已结束");
        }
        String result = JSONObject.toJSONString(questionVMList);
        examine.setResult(result);

        if(submit != null && "submit".equals(submit)){
            Long projectId = examine.getProjectId();
            Map<Long,Subject> subjectMap = subjectRepository.findAllByProjectId(projectId).stream().collect(Collectors.toMap(Subject ::getId,Function.identity()));
            int rightCount = 0;
            int total = subjectMap.size();
            for(QuestionVM questionVM : questionVMList){
                Subject subject = subjectMap.get(questionVM.getSubjectId());
                if(subject.getRight() == questionVM.getRight()){
                    examineSubjectService.doExamineSubjectCount(subject.getId());
                    rightCount++;
                }
            }
            int score = rightCount* 100/total;
            examine.setScore(score);
            examine.setStatus(ExamineStatus.FINISHED);
            Instant now = Instant.now();
            examine.setActualDuration(Integer.parseInt((now.getEpochSecond() - examine.getCreatedDate().getEpochSecond())+""));
        }
        examine = examineRepository.save(examine);
        return examineMapper.toDto(examine);
    }

    //执行任务的连接池
    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1,1,0,TimeUnit.MINUTES,new LinkedBlockingDeque<Runnable>());

    private final ThreadPoolExecutor threadPoolMonthExecutor = new ThreadPoolExecutor(1,1,0,TimeUnit.MINUTES,new LinkedBlockingDeque<Runnable>());


    private final Logger log = LoggerFactory.getLogger(ExamineService.class);
    /**
     * 答题时间结束  定时任务
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void answerTimeOut(){
        if(threadPoolExecutor.getActiveCount() > 0){
            log.info("---------------------有答题时间结束定时任务执行中，本次不执行--------------");
            return;
        }
        List<Examine> examineList = examineRepository.findAllByStatusAndDelFlag(ExamineStatus.NORMAL,DeleteFlag.NORMAL);
        log.info("------------开始执行答题时间结束定时任务------------------");
        threadPoolExecutor.execute(new AnswerTimeOutScheduledTask(examineList,subjectRepository,examineRepository));
    }

    /**
     * 每月1号 凌晨0点1分执行
     */
    @Scheduled(cron = "0 1 0 1 * ?")
    public void answerMonthOut(){
        if(threadPoolMonthExecutor.getActiveCount() > 0){
            log.info("---------------------有答题月份结束定时任务执行中，本次不执行--------------");
            return;
        }
        log.info("------------开始执行答题月份结束结束定时任务------------------");
        threadPoolMonthExecutor.execute(new AnswerMonthOutScheduledTask(examinerRepository,subjectRepository,examineRepository,projectRepository,departmentRepository));
    }
}
