package com.songzi.service;

import com.songzi.domain.Department;
import com.songzi.domain.LogBackup;
import com.songzi.domain.enumeration.DeleteFlag;
import com.songzi.domain.enumeration.Level;
import com.songzi.domain.enumeration.LogType;
import com.songzi.repository.DepartmentRepository;
import com.songzi.security.SecurityUtils;
import com.songzi.service.dto.DepartmentDTO;
import com.songzi.service.mapper.DepartmentMapper;
import com.songzi.service.mapper.DepartmentVMMapper;
import com.songzi.web.rest.vm.DepartmentQueryVM;
import com.songzi.web.rest.vm.DepartmentVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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
    private LogBackupSerivce logBackupSerivce;

    /**
     * 新建机构部门
     * @param departmentVM
     * @return
     */
    public DepartmentDTO insert(DepartmentVM departmentVM){
        Department department = departmentVMMapper.toEntity(departmentVM);
        department.setDepartmentStatus("NORMAL");
        department.setDepartmentType("部门");

        department = departmentRepository.save(department);

        LogBackup logBackup = new LogBackup();
        logBackup.setCreatedTime(Instant.now());
        logBackup.setCreatedBy(SecurityUtils.getCurrentUserLogin().get());
        logBackup.setDescription("创建机构"+department.getId());
        logBackup.setSize(27);
        logBackup.setLevel(Level.INFO);
        logBackup.setAuthority("ROLE_ADMIN");
        logBackup.setLogType(LogType.SECURITY);
        logBackupSerivce.insert(logBackup);

        return departmentMapper.toDto(department);
    }

    /**
     * 更新机构部门
     * @param departmentVM
     * @return
     */
    public DepartmentDTO update(DepartmentVM departmentVM){
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
     * @param departmentQueryVM
     * @param pageable
     * @return
     */
    public Page<DepartmentDTO> getAll(DepartmentQueryVM departmentQueryVM, Pageable pageable){

        return departmentRepository.findAll(new Specification<Department>() {
            @Override
            public Predicate toPredicate(Root<Department> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();
                if(departmentQueryVM.getName() != null && !"".equals(departmentQueryVM.getName())){
                    list.add(cb.like(root.get("name").as(String.class), "%"+departmentQueryVM.getName()+"%"));
                }
                if(departmentQueryVM.getParentId() != null){
                    list.add(cb.equal(root.get("parentId").as(Long.class), departmentQueryVM.getParentId()));
                }
                list.add(cb.equal(root.get("delFlag").as(String.class), DeleteFlag.NORMAL.name()));
                Predicate[] p = new Predicate[list.size()];
                return cb.and(list.toArray(p));
            }
        },pageable).map(departmentMapper :: toDto);
    }

    public void delete(Long id){
        departmentRepository.delete(id);
        LogBackup logBackup = new LogBackup();
        logBackup.setCreatedTime(Instant.now());
        logBackup.setCreatedBy(SecurityUtils.getCurrentUserLogin().get());
        logBackup.setDescription("删除机构"+id);
        logBackup.setSize(27);
        logBackup.setLevel(Level.INFO);
        logBackup.setAuthority("ROLE_ADMIN");
        logBackup.setLogType(LogType.SECURITY);
        logBackupSerivce.insert(logBackup);
    }
}
