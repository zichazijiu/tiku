package com.songzi.service.mapper;

import com.songzi.domain.Examiner;
import com.songzi.service.dto.ExaminerDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface ExaminerMapper extends EntityMapper<ExaminerDTO, Examiner>{

    default Examiner fromId(Long id) {
        if (id == null) {
            return null;
        }
        Examiner examiner = new Examiner();
        examiner.setId(id);
        return examiner;
    }
}
