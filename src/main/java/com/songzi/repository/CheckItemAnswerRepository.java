package com.songzi.repository;

import com.songzi.domain.CheckItemAnswer;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the CheckItemAnswer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CheckItemAnswerRepository extends JpaRepository<CheckItemAnswer, Long>, CheckItemAnswerRepositoryCustom {

}
