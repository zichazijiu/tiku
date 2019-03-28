package com.songzi.service;

import com.songzi.domain.Department;
import com.songzi.domain.Examiner;
import com.songzi.domain.LogBackup;
import com.songzi.domain.User;
import com.songzi.domain.enumeration.DeleteFlag;
import com.songzi.domain.enumeration.Level;
import com.songzi.domain.enumeration.LogType;
import com.songzi.repository.DepartmentRepository;
import com.songzi.repository.ExaminerRepository;
import com.songzi.repository.UserRepository;
import com.songzi.security.AuthoritiesConstants;
import com.songzi.security.SecurityUtils;
import com.songzi.service.dto.DepartmentDTO;
import com.songzi.service.dto.UserDTO;
import com.songzi.service.mapper.DepartmentMapper;
import com.songzi.service.mapper.DepartmentVMMapper;
import com.songzi.service.mapper.UserMapper;
import com.songzi.web.rest.errors.BadRequestAlertException;
import com.songzi.web.rest.util.PaginationUtil;
import com.songzi.web.rest.vm.DepartmentQueryVM;
import com.songzi.web.rest.vm.DepartmentVM;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sun.rmi.runtime.Log;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class DepartmentSerivce {

    @Autowired
    private DepartmentVMMapper departmentVMMapper;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private ExaminerRepository examinerRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 新建机构部门
     *
     * @param departmentVM
     * @return
     */
    public DepartmentDTO insert(DepartmentVM departmentVM) {
        Department department = departmentVMMapper.toEntity(departmentVM);
        department.setDepartmentStatus("NORMAL");
        department.setDepartmentType("部门");

        department = departmentRepository.save(department);
        return departmentMapper.toDto(department);
    }

    /**
     * 更新机构部门
     *
     * @param departmentVM
     * @return
     */
    public DepartmentDTO update(DepartmentVM departmentVM) {
        Department department = departmentRepository.findOne(departmentVM.getId());

        department.setCode(departmentVM.getCode());
        department.setName(departmentVM.getName());
        department.setDelFlag(departmentVM.getDelFlag());
        department.setDepartmentStatus("NORMAL");
        department.setDepartmentType("部门");
        department.setParentId(departmentVM.getParentId());

        return departmentMapper.toDto(departmentRepository.save(department));
    }

    /**
     * 根据条件查询机构部门
     *
     * @param departmentQueryVM
     * @return
     */
    public List<DepartmentDTO> getAll(DepartmentQueryVM departmentQueryVM) {

        return departmentRepository.findAll(new Specification<Department>() {
            @Override
            public Predicate toPredicate(Root<Department> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();
                if (departmentQueryVM.getName() != null && !"".equals(departmentQueryVM.getName())) {
                    list.add(cb.like(root.get("name").as(String.class), "%" + departmentQueryVM.getName() + "%"));
                }
                if (departmentQueryVM.getParentId() != null) {
                    list.add(cb.equal(root.get("parentId").as(Long.class), departmentQueryVM.getParentId()));
                }
                list.add(cb.equal(root.get("delFlag").as(String.class), DeleteFlag.NORMAL.name()));
                Predicate[] p = new Predicate[list.size()];
                return cb.and(list.toArray(p));
            }
        }).stream().map(departmentMapper::toDto).collect(Collectors.toList());
    }

    public void delete(Long id) {
        List<Examiner> examinerList = examinerRepository.findAllByDepartmentId(id);
        if (examinerList != null && examinerList.size() > 0) {
            throw new BadRequestAlertException("本机构下有考察员不允许删除", this.getClass().getName(), "不能删除");
        }

        List<Department> departmentList = departmentRepository.findAllByParentIdAndDelFlag(id, DeleteFlag.NORMAL);
        if (departmentList != null && departmentList.size() > 0) {
            throw new BadRequestAlertException("本机构下有其他机构不允许删除", this.getClass().getName(), "不能删除");
        }
        departmentRepository.delete(id);
    }

    /**
     * 根据用户ID查找部门树
     *
     * @param userId
     * @return
     */
    public List<Department> getDepartmentTreeByUserId(Long userId) {
        Department department = this.getDepartmentByUserId(userId);
        if (department != null) {
            String[] szParentCodes = department.getParentCodes().split(",");
            Set<String> parentCodes = Stream.of(szParentCodes).collect(Collectors.toSet());
            return departmentRepository.findDepartmentTreeByCodes(parentCodes);
        }
        return null;
    }

    /**
     * 根据用户ID获取部门信息
     *
     * @param userId
     * @return
     */
    public Department getDepartmentByUserId(Long userId) {
        return departmentRepository.findOneByUserId(userId);
    }

    /**
     * 根据部门ID查询子部门
     *
     * @param deptId
     * @param level
     * @return
     */
    public List<Department> getChildDepartmentById(Long deptId, int level) {
        Department department = departmentRepository.findOne(deptId);
        if (department == null) {
            throw new BadRequestAlertException("部门不存在", this.getClass().getName(), "部门不存在");
        }
        String codePrefix = "";
        for (int i = 0; i < level; i++) {
            codePrefix += "__";
        }
        return departmentRepository.findChildDepartmentByDepartmentCode(DeleteFlag.NORMAL.name(), department.getCode() + codePrefix);
    }

    /**
     * 更新部门用户
     *
     * @param departmentId
     * @param userIds
     * @return
     */
    public void updateDepartmentUser(Long departmentId, Long[] userIds) {
        Department department = departmentRepository.findOne(departmentId);
        if (department == null) {
            throw new BadRequestAlertException("部门不存在", this.getClass().getName(), "部门不存在");
        }
        List<User> userList = new ArrayList<>(16);
        for (int i = 0; i < userIds.length; i++) {
            Long userId = userIds[i];

            Long deptId = departmentRepository.findDepartmentIdByUserId(userId);
            if (deptId == null) {
                // 插入一条记录
                departmentRepository.insertDepartmentIdAndUserId(userId, departmentId);
            } else {
                if (departmentId.longValue() != deptId.longValue()) {
                    // 部门ID不同更新记录
                    departmentRepository.updateDepartmentIdByUserId(departmentId, userId);
                }
            }
        }
    }

    /**
     * 根据部门ID获取用户信息
     *
     * @param pageable
     * @param departmentId
     * @return
     */
    public Page<UserDTO> findAllByDepartment(Pageable pageable, Long departmentId) {
        Department department = departmentRepository.findOne(departmentId);
        return userRepository.findAllByDepartment(pageable, department).map(UserDTO::new);
    }

    /**
     * 根据登录用户获取部门列表
     *
     * @return
     */
    public List<DepartmentDTO> finAllByUser() {
        User user = SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin).get();
        Set<String> roles = user.getAuthorities().stream().map(x -> x.getName()).collect(Collectors.toSet());
        if (roles.contains(AuthoritiesConstants.ADMIN)) {
            return departmentRepository.findAllByDelFlagIs(DeleteFlag.NORMAL)
                .stream()
                .map(departmentMapper::toDto)
                .collect(Collectors.toList());
        } else if (roles.contains(AuthoritiesConstants.BU_ADMIN)
            || roles.contains(AuthoritiesConstants.TING_ADMIN)
            || roles.contains(AuthoritiesConstants.JU_ADMIN)
            || roles.contains(AuthoritiesConstants.CHU_ADMIN)) {
            Department department = user.getDepartment();
            if (department == null) {
                throw new BadRequestAlertException("该用户的部门不存在", this.getClass().getName(), "部门不存在");
            }
            // 替换code后四位为____
            int position = department.getCode().length();
            String code = new StringBuilder(department.getCode()).replace(position - 4, position, "----").toString();
            return departmentRepository.findChildDepartmentByDepartmentCode(DeleteFlag.NORMAL.name(), code)
                .stream().map(departmentMapper::toDto).collect(Collectors.toList());
        } else {
            Department department = user.getDepartment();
            if (department == null) {
                throw new BadRequestAlertException("该用户的部门不存在", this.getClass().getName(), "部门不存在");
            }
            return departmentRepository.findChildDepartmentByDepartmentCode(DeleteFlag.NORMAL.name(), department.getCode())
                .stream().map(departmentMapper::toDto).collect(Collectors.toList());
        }
    }
}
