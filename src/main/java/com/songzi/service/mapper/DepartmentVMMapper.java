package com.songzi.service.mapper;

import com.songzi.domain.Department;
import com.songzi.web.rest.vm.DepartmentVM;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface DepartmentVMMapper extends EntityMapper<DepartmentVM, Department>{

    default Department fromId(Long id) {
        if (id == null) {
            return null;
        }
        Department department = new Department();
        department.setId(id);
        return department;
    }
}
