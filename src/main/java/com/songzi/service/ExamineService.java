package com.songzi.service;

import com.songzi.domain.Department;
import com.songzi.domain.Examine;
import com.songzi.domain.Examiner;
import com.songzi.domain.Project;
import com.songzi.domain.enumeration.DeleteFlag;
import com.songzi.repository.*;
import com.songzi.service.dto.ExamineDTO;
import com.songzi.service.mapper.ExamineMapper;
import com.songzi.service.mapper.ExamineSubjectVMMapper;
import com.songzi.service.mapper.ExamineVMMapper;
import com.songzi.service.mapper.ProjectMapper;
import com.songzi.web.rest.vm.ExamineVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    /**
     *
     * @param examineVM
     * @return
     */
    public ExamineDTO insert(ExamineVM examineVM){

        Examine examine = examineVMMapper.toEntity(examineVM);
        examine.setDelFlag(DeleteFlag.NORMAL);
        examine.setUserId(userService.getCurrentUserId());
        Examiner examiner = examinerRepository.getOneByUserId(userService.getCurrentUserId());
        examine.setDepartmentId(examiner.getDepartmentId());
        Department department = departmentRepository.findOne(examiner.getDepartmentId());
        Project project = projectRepository.findOne(examineVM.getProjectId());
        examine.setName(department.getName()+"-"+examiner.getName()+ project.getName() + "-" +"的考试");
        examine.setScore(0);
        examine.setDuration(project.getDuration());
        examine.setStatus("0");
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
}
