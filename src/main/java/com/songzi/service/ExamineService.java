package com.songzi.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.songzi.domain.*;
import com.songzi.domain.enumeration.DeleteFlag;
import com.songzi.domain.enumeration.ExamineStatus;
import com.songzi.domain.enumeration.ExamineType;
import com.songzi.repository.*;
import com.songzi.service.dto.ExamineDTO;
import com.songzi.service.dto.ExamineSubjectDTO;
import com.songzi.service.dto.MultipleChoice;
import com.songzi.service.mapper.ExamineMapper;
import com.songzi.service.mapper.ExamineSubjectVMMapper;
import com.songzi.service.mapper.ExamineVMMapper;
import com.songzi.service.mapper.ProjectMapper;
import com.songzi.util.JSONUtils;
import com.songzi.web.rest.errors.BadRequestAlertException;
import com.songzi.web.rest.errors.ErrorConstants;
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
    private ExamineSubjectService examineSubjectService;

    private static final String SUBMIT = "submit";

    /**
     * @param examineVM
     * @return
     */
    public ExamineDTO insert(ExamineVM examineVM) {
        Long userId = userService.getCurrentUserId();
        List<Examine> examineOld = examineRepository.findAllByProjectIdAndDelFlagAndUserId(examineVM.getProjectId(), DeleteFlag.NORMAL, userId);
        if (examineOld != null && examineOld.size() > 0) {
            throw new BadRequestAlertException("该项目已经有正在答题的考试，同一项目不允许有多场考试", this.getClass().getName(), "不允许有多场考试");
        }
        Examine examine = examineVMMapper.toEntity(examineVM);
        examine.setDelFlag(DeleteFlag.NORMAL);
        examine.setUserId(userService.getCurrentUserId());
        Examiner examiner = examinerRepository.findOneByUserId(userService.getCurrentUserId());
        examine.setDepartmentId(examiner.getDepartmentId());
        Department department = departmentRepository.findOne(examiner.getDepartmentId());
        Project project = projectRepository.findOne(examineVM.getProjectId());
        if (project == null)
            throw new BadRequestAlertException("项目ID[" + examineVM.getProjectId() + "]不不存在，请联系管理员", this.getClass().getName(), "项目不存在");
        examine.setName(department.getName() + "-" + examiner.getName() + project.getName() + "-" + "的考试");
        examine.setScore(null);
        examine.setDuration(project.getDuration());
        examine.setStatus(ExamineStatus.NORMAL);
        examine.setType(examineVM.getExamineType());
        examine = examineRepository.save(examine);

        ExamineDTO examineDTO = examineMapper.toDto(examine);
        examineDTO.setProject(projectMapper.toDto(project));
        examineDTO.setExamineSubjectDTOList(listExamineSubject(project.getId()));

        return examineDTO;
    }

    /**
     * @param examineVM
     * @return
     */
    public Examine update(ExamineVM examineVM) {
        Examine examine = examineRepository.findOne(examineVM.getId());
        examine.setProjectId(examineVM.getProjectId());
        return examineRepository.save(examine);
    }

    /**
     * @param pageable
     * @return
     */
    public Page<ExamineDTO> getAll(Pageable pageable) {
        return examineRepository.findAllByDelFlag(DeleteFlag.NORMAL, pageable)
            .map(examineMapper::toDto);
    }

    public ExamineDTO getOne(Long id) {
        Examine examine = examineRepository.findOne(id);
        if (examine == null)
            throw new BadRequestAlertException("考试[" + id + "]不存在", this.getClass().getName(), "考试不存在");
        Examiner examiner = examinerRepository.findOneByUserId(userService.getCurrentUserId());
        Department department = departmentRepository.findOne(examiner.getDepartmentId());
        Project project = projectRepository.findOne(examine.getProjectId());

        ExamineDTO examineDTO = examineMapper.toDto(examine);
        examineDTO.setProject(projectMapper.toDto(project));
        examineDTO.setExamineSubjectDTOList(listExamineSubject(project.getId()));

        return examineDTO;
    }

    /**
     * 根据项目ID获取考试题目列表
     *
     * @param projectId 项目ID
     * @return
     */
    private List<ExamineSubjectDTO> listExamineSubject(Long projectId) {
        return subjectRepository.findAllByProjectId(projectId)
            .stream()
            .map(examineSubjectVMMapper::toDto)
            .map(examineSubjectDTO -> {
                // 格式化multipleChoices
                String options = examineSubjectDTO.getOptions();
                if (JSONUtils.isJSONFormat(options)) {
                    List<MultipleChoice> multipleChoices = JSON.parseArray(options, MultipleChoice.class);
                    examineSubjectDTO.setMultipleChoices(multipleChoices);
                    // 清除options
                    examineSubjectDTO.setOptions("");
                }
                return examineSubjectDTO;
            }).collect(Collectors.toList());
    }

    /**
     * 提交答案
     *
     * @param examineId
     * @param submit
     * @param questionVMList
     * @return
     */
    public ExamineDTO answer(Long examineId, String submit, List<QuestionVM> questionVMList) {
        Examine examine = examineRepository.findOne(examineId);
        if (examine.getStatus() == ExamineStatus.FINISHED) {
            throw new BadRequestAlertException("该场考试已结束，不能再次提交答案", this.getClass().getName(), "考试已结束");
        }
        String result = JSONObject.toJSONString(questionVMList);
        examine.setResult(result); // 保存答案

        // 检查是否提交答案
        if (SUBMIT.equals(submit)) { // 判断答案分数统计

            float score = calculateGrades(examine, questionVMList);
            examine.setScore(score);
            examine.setStatus(ExamineStatus.FINISHED);
            Instant now = Instant.now();
            examine.setActualDuration(Integer.parseInt((now.getEpochSecond() - examine.getCreatedDate().getEpochSecond()) + ""));
            Long userId = examine.getUserId();
            Examiner examiner = examinerRepository.findOneByUserId(userId);
            int times = examiner.getTime();
            examiner.setTime(times + 1);
            examinerRepository.save(examiner);
            examine = examineRepository.save(examine);

        }
        return examineMapper.toDto(examine);
    }

    /**
     * 计算考试得分
     *
     * @param examine 考试
     * @return
     */
    public float calculateGrades(Examine examine, List<QuestionVM> questionVMList) {
        float grades = 0;
        // 1. 获取考试类型
        ExamineType examineType = examine.getType();
        // 2. 获取考试题目
        Map<Long, Subject> subjectMap = subjectRepository.findAllByProjectId(examine.getProjectId())
            .stream()
            .collect(Collectors.toMap(Subject::getId, Function.identity()));
        switch (examineType) {
            case MULTIPLE_CHOICE:

                // 1. 重置总分为0
                grades = 0;
                for (QuestionVM questionVM : questionVMList) {
                    // 获取考试题
                    Subject subject = subjectMap.get(questionVM.getSubjectId());
                    float subjectTotalPoint = Float.parseFloat(subject.getTotalPoint()); // 考题总分
                    List<MultipleChoice> multipleChoices = JSON.parseArray(subject.getOptions(), MultipleChoice.class);
                    String[] answers = questionVM.getMultipleChoiceAnswers();
                    // 核对选项答案
                    if (answers != null) {
                        for (int i = 0; i < answers.length; i++) {
                            String no = answers[i];// 项目号
                            // 获取ordinal指定选项号的分点
                            MultipleChoice multipleChoice = multipleChoices.get(Integer.parseInt(no) - 1); // 标准答案
                            log.info(questionVM.getSubjectId() + "题的第" + multipleChoice.getNo() + "项的分值是" + multipleChoice.getPoint());
                            // 运算表示
                            String computeFlag = multipleChoice.getPoint().substring(0, 1);
                            // 分点
                            float point = Math.abs(Float.parseFloat(multipleChoice.getPoint()));
                            if ("+".equals(computeFlag))
                                subjectTotalPoint += point;
                            else
                                subjectTotalPoint -= point;

                            // 如果项目成了负值，该项0分
                            if (subjectTotalPoint <= 0) {
                                subjectTotalPoint = 0;
                                break;
                            }
                        } // 一道考题题判卷结束
                    }
                    // 累加总分
                    grades += subjectTotalPoint;
                    // 薄弱项目记录
                    // 满分 正确计一个
                    if (subjectTotalPoint == Float.parseFloat(subject.getTotalPoint()))
                        examineSubjectService.doExamineSubjectCount(subject.getId(), examine.getDepartmentId());
                    else // 否则 错误计一个
                        examineSubjectService.doExamineSubjectWrongCount(subject.getId(), examine.getDepartmentId());
                } // 所有考题判卷结束

                break;
            case TRUE_OR_FALSE:
                int rightCount = 0;
                for (QuestionVM questionVM : questionVMList) {
                    Subject subject = subjectMap.get(questionVM.getSubjectId());
                    if (subject.getRight() == questionVM.getRight()) {
                        examineSubjectService.doExamineSubjectCount(subject.getId(), examine.getDepartmentId());
                        rightCount++;
                    } else {
                        examineSubjectService.doExamineSubjectWrongCount(subject.getId(), examine.getDepartmentId());
                    }
                }
                grades = rightCount * 100 / subjectMap.size();
                break;
            case UNKNOWN:
                break;
            default:
                break;
        }
        return grades;
    }

    //执行任务的连接池
    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MINUTES, new LinkedBlockingDeque<Runnable>());

    private final ThreadPoolExecutor threadPoolMonthExecutor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MINUTES, new LinkedBlockingDeque<Runnable>());


    private final Logger log = LoggerFactory.getLogger(ExamineService.class);

    /**
     * 答题时间结束  定时任务
     */
//    @Scheduled(cron = "0 */1 * * * ?")
//    public void answerTimeOut() {
//        if (threadPoolExecutor.getActiveCount() > 0) {
//            log.info("---------------------有答题时间结束定时任务执行中，本次不执行--------------");
//            return;
//        }
//        List<Examine> examineList = examineRepository.findAllByStatusAndDelFlag(ExamineStatus.NORMAL, DeleteFlag.NORMAL);
//        log.info("------------开始执行答题时间结束定时任务------------------");
//        threadPoolExecutor.execute(new AnswerTimeOutScheduledTask(examineList, subjectRepository, examineRepository, examinerRepository));
//    }

    /**
     * 每月1号 凌晨0点1分执行
     */
    @Scheduled(cron = "0 1 0 1 * ?")
    public void answerMonthOut() {
        if (threadPoolMonthExecutor.getActiveCount() > 0) {
            log.info("---------------------有答题月份结束定时任务执行中，本次不执行--------------");
            return;
        }
        log.info("------------开始执行答题月份结束结束定时任务------------------");
        threadPoolMonthExecutor.execute(new AnswerMonthOutScheduledTask(examinerRepository, subjectRepository, examineRepository, projectRepository, departmentRepository, examineSubjectService));
    }
}
