package com.songzi.repository;

import com.songzi.domain.ExamineSubject;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the ExamineSubject entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExamineSubjectRepository extends JpaRepository<ExamineSubject, Long> {

    ExamineSubject findOneBySubjectIdAndYearAndMonthAndDepartmentId(Long subjectId,String year,String month,Long departmentId);

    @Query(value = "select COALESCE(sum(e.wrongTime),0) from ExamineSubject e where e.year = ?1 and e.month =?2")
    Integer countWrongTimeByYearAndMonth(String year,String month);

    @Query(value = "select COALESCE(sum(e.wrongTime),0) from ExamineSubject e where e.year = ?1 and e.month =?2 and e.departmentId =?3")
    Integer countWrongTimeByYearAndMonthAndDepartmentId(String year,String month,Long departmentId);

    @Query(value = "select COALESCE(sum(e.wrongTime),0),e.subjectId from ExamineSubject e where e.year =?1 and e.month =?2 GROUP BY e.subjectId order by COALESCE(sum(e.wrongTime),0) desc")
    List<Object[]> findAllByYearAndMonthOrderByWrongTimeDesc(String year,String month,Pageable pageable);

    @Query(value = "select COALESCE(sum(e.wrongTime),0),e.subjectId from ExamineSubject e where e.year =?1 and e.month =?2 GROUP BY e.subjectId order by COALESCE(sum(e.wrongTime),0) desc")
    List<Object[]> findAllByYearAndMonthOrderByWrongTimeDesc(String year,String month);

    List<ExamineSubject> findAllByYearAndMonthAndDepartmentIdOrderByWrongTimeDesc(String year,String month,Long departmentId);

    List<ExamineSubject> findAllByYearAndMonthAndDepartmentIdOrderByWrongTimeDesc(String year,String month,Long departmentId,Pageable pageable);

    @Query(value = "select COALESCE(sum(e.wrongTime)+sum(e.rightTime),0) from ExamineSubject e where e.year = ?1 and e.month =?2 and e.departmentId = ?3")
    int getTotalCountTimsByDepartmentId(String year,String month,Long departmentId);

    @Query(value = "select COALESCE(sum(e.wrongTime)+sum(e.rightTime),0) from ExamineSubject e where e.year = ?1 and e.month =?2 ")
    int getTotalCountTims(String year,String month);
}
