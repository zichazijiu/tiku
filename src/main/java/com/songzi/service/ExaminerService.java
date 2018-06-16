package com.songzi.service;

import com.songzi.domain.Department;
import com.songzi.domain.Examiner;
import com.songzi.repository.DepartmentRepository;
import com.songzi.repository.ExaminerRepository;
import com.songzi.service.dto.ExaminerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing examiners.
 */
@Service
@Transactional
public class ExaminerService {

    private final Logger log = LoggerFactory.getLogger(ExaminerService.class);

    private ExaminerRepository examinerRepository;
    private UserService userService;
    private DepartmentRepository departmentRepository;

    public ExaminerService(ExaminerRepository examinerRepository, UserService userService, DepartmentRepository departmentRepository) {
        this.examinerRepository = examinerRepository;
        this.userService = userService;
        this.departmentRepository = departmentRepository;
    }

    public ExaminerDTO getCurrentExaminer() {
        Long userId = userService.getCurrentUserId();
        Examiner examiner = examinerRepository.getOneByUserId(userId);
        Department department = departmentRepository.findOne(examiner.getDepartmentId());

        ExaminerDTO examinerDTO = new ExaminerDTO();
        examinerDTO.setName(examiner.getName());
        examinerDTO.setDepartmentName(department.getName());
        examinerDTO.setDepartmentId(department.getId());
        examinerDTO.setTime(examiner.getTime());
        return examinerDTO;
    }
}
