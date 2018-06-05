package com.songzi.repository;

import com.songzi.domain.LogBackup;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the LogBackup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LogBackupRepository extends JpaRepository<LogBackup, Long> {

}
