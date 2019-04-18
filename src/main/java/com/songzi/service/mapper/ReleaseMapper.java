package com.songzi.service.mapper;

import com.songzi.domain.Release;
import com.songzi.service.dto.ReleaseDTO;
import org.mapstruct.Mapper;

/**
 * Created by Ke Qingyuan on 2019/4/18.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ReleaseMapper extends EntityMapper<ReleaseDTO, Release> {

}
