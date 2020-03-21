package com.songzi.repository;

import com.songzi.domain.DBBackup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author Ke Bei
 * @date 2020/3/21
 */
@Repository
public interface DBBackupRepository extends JpaRepository<DBBackup, Long>, JpaSpecificationExecutor<DBBackup> {

}
