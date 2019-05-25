package com.songzi.service;

import com.songzi.domain.Department;
import com.songzi.domain.User;
import com.songzi.domain.enumeration.DeleteFlag;
import com.songzi.repository.DepartmentRepository;
import com.songzi.repository.ExaminerRepository;
import com.songzi.repository.UserRepository;
import com.songzi.security.AuthoritiesConstants;
import com.songzi.security.SecurityUtils;
import com.songzi.service.dto.DepartmentDTO;
import com.songzi.service.dto.UserDTO;
import com.songzi.service.mapper.DepartmentMapper;
import com.songzi.service.mapper.DepartmentVMMapper;
import com.songzi.web.rest.errors.BadRequestAlertException;
import com.songzi.web.rest.vm.DepartmentQueryVM;
import com.songzi.web.rest.vm.DepartmentVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class DepartmentService {

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
        department.setDepartmentType("NORMAL"); // NORMAL的部门机构可以增加用户
        department = departmentRepository.save(department);
        // 如果code的长度是8或者12增加下级 "直属机关单位" "下属保密机构"
        int length = department.getCode().length();
        String codeType = department.getCode().substring(length-4);
        if (codeType.startsWith("02") && (length == 6 || length == 10)) {
            List<Department> departmentListForSaved = new ArrayList<>(2);
            // 增加"直属机关单位"
            Department dept01 = new Department();
            dept01.setName("直属机关单位");
            dept01.setParentId(department.getId());
            dept01.setCode(department.getCode() + "01");
            dept01.setParentCodes(department.getParentCodes() + "," + dept01.getCode());
            dept01.setDepartmentStatus("NORMAL");
            dept01.setDepartmentType("VIRTUAL"); // VIRTUAL的部门不能增加用户
            departmentListForSaved.add(dept01);
            // 增加"下属保密机构"
            Department dept02 = new Department();
            dept02.setName("下属保密机构");
            dept02.setParentId(department.getId());
            dept02.setCode(department.getCode() + "02");
            dept02.setParentCodes(department.getParentCodes() + "," + dept01.getCode());
            dept02.setDepartmentStatus("NORMAL");
            dept02.setDepartmentType("VIRTUAL"); // VIRTUAL的部门不能增加用户
            departmentListForSaved.add(dept02);
            // 保存部门
            departmentRepository.save(departmentListForSaved);
        }
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
        department.setDepartmentType("NORMAL");
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
//        List<Examiner> examinerList = examinerRepository.findAllByDepartmentId(id);
//        if (examinerList != null && examinerList.size() > 0) {
//            throw new BadRequestAlertException("本机构下有考察员不允许删除", this.getClass().getName(), "不能删除");
//        }

        // 查询用户
        Department department = departmentRepository.findOne(id);
        if (department != null) {
            List<User> userList = userRepository.findAllByDepartment(department);
            if (userList != null && userList.size() > 0) {
                throw new BadRequestAlertException("本机构下有用户不允许删除", this.getClass().getName(), "不能删除");
            }
        }
        // 检查是由有其它机构
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
        String codePrefix = "__";
        for (int i = 1; i < level; i++) {
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
     * 根据部门ID获取用户信息包含子部门
     *
     * @param pageable
     * @param departmentId
     * @return
     */
    public Page<UserDTO> findAllByDepartmentWithChild(Pageable pageable, Long departmentId) {
        // 获取当前用户的部门ID
        Department department = departmentRepository.findOne(departmentId);
        // 获取子部门信息
        List<Department> departmentList = departmentRepository.findAllChildDepartmentByCode(DeleteFlag.NORMAL.toString(), department.getCode());

        return userRepository.findAllByDepartmentIn(pageable, departmentList).map(UserDTO::new);
    }

    /**
     * 根据登录用户获取部门列表
     *
     * @return
     */
    public List<DepartmentDTO> finAllByUser() {
        User user = SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin).get();
        Set<String> roles = user.getAuthorities().stream().map(x -> x.getName()).collect(Collectors.toSet());
        if (roles.contains(AuthoritiesConstants.ADMIN) || roles.contains(AuthoritiesConstants.BU_ADMIN)) {
            return departmentRepository.findAllByDelFlagIs(DeleteFlag.NORMAL)
                .stream()
                .map(departmentMapper::toDto)
                .collect(Collectors.toList());
        } else if (roles.contains(AuthoritiesConstants.TING_ADMIN)) {
            return getChildDepartmentListByCode(user.getDepartment(), 6);
        } else if (roles.contains(AuthoritiesConstants.JU_ADMIN)) {
            return getChildDepartmentListByCode(user.getDepartment(), 10);
        } else if (roles.contains(AuthoritiesConstants.CHU_ADMIN)) {
            return getChildDepartmentListByCode(user.getDepartment(), 14);
        } else {
            Department department = user.getDepartment();
            if (department == null) {
                throw new BadRequestAlertException("该用户的部门不存在", this.getClass().getName(), "部门不存在");
            }
            return departmentRepository.findChildDepartmentByDepartmentCode(DeleteFlag.NORMAL.name(), department.getCode())
                .stream().map(departmentMapper::toDto).collect(Collectors.toList());
        }
    }

    /**
     * 指定code长度获取子部门信息
     *
     * @param department
     * @param expect
     * @return
     */
    private List<DepartmentDTO> getChildDepartmentListByCode(Department department, int expect) {
        if (department == null) {
            throw new BadRequestAlertException("该用户的部门不存在", this.getClass().getName(), "部门不存在");
        }
        String code = department.getCode();
        if (code.length() > expect) {
            code = code.substring(0, expect);
        }
        return departmentRepository.findAllChildDepartmentByCode(DeleteFlag.NORMAL.name(), code)
            .stream().map(departmentMapper::toDto).collect(Collectors.toList());
    }

    /**
     * 根据用户部门查询子部门的用户信息
     *
     * @param department
     * @return
     */
    public List<User> getChildDepartmentUserByDepartment(Department department) {
        if (department != null) {
            // 获取子部门信息
            List<Department> departmentList = departmentRepository.findAllChildDepartmentByCode(DeleteFlag.NORMAL.toString(), department.getCode());
            return userRepository.findAllByDepartmentIn(departmentList);
        }
        return null;
    }

    /**
     * 根据部门Code查询子部门用户信息
     *
     * @param code
     * @return
     */
    public List<User> getChildDepartmentUserByDepartmentCode(String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        } else {
            List<Department> departments = departmentRepository.findChildDepartmentByDepartmentCode(DeleteFlag.NORMAL.toString(), code);
            return userRepository.findAllByDepartmentIn(departments);
        }
    }

    /**
     * 根据创建者查询部门信息
     *
     * @return
     */
    public List<DepartmentDTO> findAllByCreatedUser(User createdUser) {
        if (createdUser == null) {
            throw new BadRequestAlertException("用户不存在", this.getClass().getName(), "用户不存在");
        }

        String login = createdUser.getLogin();
        return departmentRepository.findAllByDelFlagAndCreatedBy(DeleteFlag.NORMAL, login)
            .stream()
            .map(departmentMapper::toDto)
            .collect(Collectors.toList());

    }
}
