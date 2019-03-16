package com.songzi.repository;

import com.songzi.domain.RemainsQuestion;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the RemainsQuestion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RemainsQuestionRepository extends JpaRepository<RemainsQuestion, Long> {

}
