package com.songzi.service;

import com.alibaba.fastjson.JSONObject;
import com.songzi.domain.*;
import com.songzi.domain.enumeration.*;
import com.songzi.repository.*;
import com.songzi.security.SecurityUtils;
import com.songzi.service.dto.ExamineDTO;
import com.songzi.service.mapper.ExamineMapper;
import com.songzi.service.mapper.ExamineSubjectVMMapper;
import com.songzi.service.mapper.ExamineVMMapper;
import com.songzi.service.mapper.ProjectMapper;
import com.songzi.web.rest.errors.BadRequestAlertException;
import com.songzi.web.rest.vm.ExamineVM;
import com.songzi.web.rest.vm.QuestionVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Map;
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

    /**
     *
     * @param examineVM
     * @return
     */
    public ExamineDTO insert(ExamineVM examineVM){
        List<Examine> examineOld = examineRepository.findAllByProjectIdAndDelFlag(examineVM.getProjectId(),DeleteFlag.NORMAL);
        if(examineOld != null && examineOld.size() > 0){
            throw new BadRequestAlertException("该项目已经有正在答题的考试，同一项目不允许有多场考试",this.getClass().getName(),"不允许有多场考试");
        }
        Examine examine = examineVMMapper.toEntity(examineVM);
        examine.setDelFlag(DeleteFlag.NORMAL);
        examine.setUserId(userService.getCurrentUserId());
        Examiner examiner = examinerRepository.getOneByUserId(userService.getCurrentUserId());
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
        Examiner examiner = examinerRepository.getOneByUserId(userService.getCurrentUserId());
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
                    rightCount++;
                }
            }
            int score = rightCount* 100/total;
            examine.setScore(score);
            examine.setStatus(ExamineStatus.FINISHED);
        }
        examine = examineRepository.save(examine);
        return examineMapper.toDto(examine);
    }
}
