package com.songzi.repository;

import com.songzi.domain.ExamineSubject;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the ExamineSubject entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExamineSubjectRepository extends JpaRepository<ExamineSubject, Long> {

    ExamineSubject findOneBySubjectIdAndYearAndMonth(Long subjectId,String year,String month);

    Integer countAllByYearAndMonth(String year,String month);

    List<ExamineSubject> findAllByYearAndMonth(String year,String month);
}
