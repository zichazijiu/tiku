package com.songzi.service.mapper;

import com.songzi.domain.Examine;
import com.songzi.service.dto.ExamineDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ProjectMapper.class})
public interface ExamineMapper extends EntityMapper<ExamineDTO, Examine>{

    default Examine fromId(Long id) {
        if (id == null) {
            return null;
        }
        Examine examine = new Examine();
        examine.setId(id);
        return examine;
    }
}
