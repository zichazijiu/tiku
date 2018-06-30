package com.songzi.service.mapper;

import com.songzi.domain.Project;
import com.songzi.domain.Subject;
import com.songzi.service.dto.ProjectDTO;
import com.songzi.service.dto.SubjectDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface SubjectMapper extends EntityMapper<SubjectDTO, Subject>{

    default Subject fromId(Long id) {
        if (id == null) {
            return null;
        }
        Subject subject = new Subject();
        subject.setId(id);
        return subject;
    }
}
