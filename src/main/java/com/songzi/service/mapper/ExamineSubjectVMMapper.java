package com.songzi.service.mapper;

import com.songzi.domain.Subject;
import com.songzi.service.dto.ExamineSubjectDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface ExamineSubjectVMMapper extends EntityMapper<ExamineSubjectDTO, Subject>{

    default Subject fromId(Long id) {
        if (id == null) {
            return null;
        }
        Subject subject = new Subject();
        subject.setId(id);
        return subject;
    }
}
