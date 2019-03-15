package com.songzi.repository;

import com.songzi.domain.RemainsQuestion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RemainsQuestion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RemainsQuestionRepository extends JpaRepository<RemainsQuestion, Long> {

}
