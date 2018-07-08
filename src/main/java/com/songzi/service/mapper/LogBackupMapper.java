package com.songzi.service.mapper;

import com.songzi.domain.LogBackup;
import com.songzi.service.dto.LogBackupDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface LogBackupMapper extends EntityMapper<LogBackupDTO, LogBackup>{

    default LogBackup fromId(Long id) {
        if (id == null) {
            return null;
        }
        LogBackup logBackup = new LogBackup();
        logBackup.setId(id);
        return logBackup;
    }
}
