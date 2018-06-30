package com.songzi.service;

import com.songzi.domain.Department;
import com.songzi.domain.Examiner;
import com.songzi.domain.enumeration.DeleteFlag;
import com.songzi.repository.DepartmentRepository;
import com.songzi.repository.ExaminerRepository;
import com.songzi.service.dto.ExaminerDTO;
import com.songzi.service.mapper.ExaminerMapper;
import com.songzi.service.mapper.ExaminerVMMapper;
import com.songzi.web.rest.vm.ExaminerQueryVM;
import com.songzi.web.rest.vm.ExaminerVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private ExaminerVMMapper examinerVMMapper;

    @Autowired
    private ExaminerMapper examinerMapper;

    public ExaminerService(ExaminerRepository examinerRepository, UserService userService, DepartmentRepository departmentRepository) {
        this.examinerRepository = examinerRepository;
        this.userService = userService;
        this.departmentRepository = departmentRepository;
    }

    /**
     * 新建考评员
     * @param examinerVM
     * @return
     */
    public Examiner insert(ExaminerVM examinerVM){
        Examiner examiner = examinerVMMapper.toEntity(examinerVM);

        examiner.setUserId(userService.getCurrentUserId());
        examiner.setTime(0);

        return examinerRepository.save(examiner);
    }

    /**
     * 更新考评员
     * @return
     */
    public Examiner update(ExaminerVM examinerVM){
        Examiner examiner = examinerRepository.findOne(examinerVM.getId());

        examiner.setName(examinerVM.getName());
        examiner.setDepartmentId(examinerVM.getDepartmentId());

        //自动关联到当前登录用户上
        examiner.setUserId(userService.getCurrentUserId());
        //更新用户数据的时候不会更新其对应的考评次数
        //examiner.setTime(0);

        examiner.setCellPhone(examinerVM.getCellPhone());
        examiner.setEmail(examinerVM.getEmail());
        examiner.setSex(examinerVM.getSex());
        examiner.setBirth(examinerVM.getBirth());
        examiner.setLocation(examinerVM.getLocation());
        examiner.setPhone(examinerVM.getPhone());
        examiner.setAddress(examinerVM.getAddress());

        return examinerRepository.save(examiner);
    }

    public Page<ExaminerDTO> getAll(ExaminerQueryVM examinerQueryVM, Pageable pageable){
        return examinerRepository.findAll(new Specification<Examiner>() {
            @Override
            public Predicate toPredicate(Root<Examiner> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();
                if(examinerQueryVM.getName() != null && !"".equals(examinerQueryVM.getName())){
                    list.add(cb.like(root.get("name").as(String.class), "%"+examinerQueryVM.getName()+"%"));
                }
                if(examinerQueryVM.getDepartMentId() != null){
                    list.add(cb.equal(root.get("parentId").as(Long.class), examinerQueryVM.getDepartMentId()));
                }
                Predicate[] p = new Predicate[list.size()];
                return cb.and(list.toArray(p));
            }
        },pageable).map(examinerMapper :: toDto).map(examinerDTO -> {
            Department department = departmentRepository.findOne(examinerDTO.getDepartmentId());
            examinerDTO.setDepartmentName(department.getName());
            return examinerDTO;
        });
    }

    /**
     * 通过Id获取考评员信息
     * @param userId
     * @return
     */
    public ExaminerDTO getOneById(Long userId) {
        Examiner examiner = examinerRepository.getOneByUserId(userId);
        Department department = departmentRepository.findOne(examiner.getDepartmentId());

        ExaminerDTO examinerDTO = examinerMapper.toDto(examiner);
        examinerDTO.setDepartmentName(department.getName());

        return examinerDTO;
    }


    /**
     * 获取当前考评员的信息
     * @return
     */
    public ExaminerDTO getCurrentExaminer() {
        Long userId = userService.getCurrentUserId();
        Examiner examiner = examinerRepository.getOneByUserId(userId);
        Department department = departmentRepository.findOne(examiner.getDepartmentId());

        ExaminerDTO examinerDTO = examinerMapper.toDto(examiner);
        examinerDTO.setDepartmentName(department.getName());

        return examinerDTO;
    }


}
