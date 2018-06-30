package com.songzi.service.mapper;

import com.songzi.domain.Examiner;
import com.songzi.web.rest.vm.ExaminerVM;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface ExaminerVMMapper extends EntityMapper<ExaminerVM, Examiner>{

    default Examiner fromId(Long id) {
        if (id == null) {
            return null;
        }
        Examiner examiner = new Examiner();
        examiner.setId(id);
        return examiner;
    }
}
