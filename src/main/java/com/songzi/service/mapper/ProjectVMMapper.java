package com.songzi.service.mapper;

import com.songzi.domain.Project;
import com.songzi.web.rest.vm.ProjectVM;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface ProjectVMMapper extends EntityMapper<ProjectVM, Project>{

    default Project fromId(Long id) {
        if (id == null) {
            return null;
        }
        Project project = new Project();
        project.setId(id);
        return project;
    }
}
