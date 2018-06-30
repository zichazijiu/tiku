package com.songzi.service;

import com.songzi.domain.Department;
import com.songzi.domain.enumeration.DeleteFlag;
import com.songzi.repository.DepartmentRepository;
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
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DepartmentSerivce {

    @Autowired
    private DepartmentVMMapper departmentVMMapper;

    @Autowired
    private DepartmentRepository departmentRepository;

    /**
     * 新建机构部门
     * @param departmentVM
     * @return
     */
    public Department insert(DepartmentVM departmentVM){
        Department department = departmentVMMapper.toEntity(departmentVM);
        if(department.getParentId() == null){
            department.setParentId(0L);
        }
        return departmentRepository.save(department);
    }

    /**
     * 更新机构部门
     * @param departmentVM
     * @return
     */
    public Department update(DepartmentVM departmentVM){
        Department department = departmentRepository.findOne(departmentVM.getId());

        department.setCode(departmentVM.getCode());
        department.setName(departmentVM.getName());
        department.setDelFlag(departmentVM.getDelFlag());
        department.setDepartmentStatus(departmentVM.getDepartmentStatus());
        department.setDepartmentType(departmentVM.getDepartmentType());
        department.setParentId(departmentVM.getParentId());

        return departmentRepository.save(department);
    }

    /**
     * 根据条件查询机构部门
     * @param departmentQueryVM
     * @param pageable
     * @return
     */
    public Page<Department> getAll(DepartmentQueryVM departmentQueryVM, Pageable pageable){

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
        },pageable);
    }
}
