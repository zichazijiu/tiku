package com.songzi.service.mapper;

import com.songzi.domain.Examine;
import com.songzi.web.rest.vm.ExamineVM;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface ExamineVMMapper extends EntityMapper<ExamineVM, Examine>{

    default Examine fromId(Long id) {
        if (id == null) {
            return null;
        }
        Examine examine = new Examine();
        examine.setId(id);
        return examine;
    }
}
