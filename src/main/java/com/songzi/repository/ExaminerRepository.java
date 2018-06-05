package com.songzi.repository;

import com.songzi.domain.Examiner;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Examiner entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExaminerRepository extends JpaRepository<Examiner, Long> {

}
