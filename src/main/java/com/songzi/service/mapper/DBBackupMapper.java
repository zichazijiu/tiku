package com.songzi.service.mapper;

import com.songzi.domain.DBBackup;
import com.songzi.service.dto.DBBackupDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface DBBackupMapper extends EntityMapper<DBBackupDTO, DBBackup>{

    default DBBackup fromId(Long id) {
        if (id == null) {
            return null;
        }
        DBBackup dbBackup = new DBBackup();
        dbBackup.setId(id);
        return dbBackup;
    }
}
