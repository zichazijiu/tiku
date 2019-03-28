package com.songzi.repository;

import com.songzi.domain.RemainsQuestion;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Map;


/**
 * Spring Data JPA repository for the RemainsQuestion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RemainsQuestionRepository extends JpaRepository<RemainsQuestion, Long> {

    /**
     * 根据组织编号进行问题分布统计
     * @param departmentId
     * @return
     */
    @Query(value = "SELECT DATE_FORMAT(remains_question.created_time,'%Y-%m-%d') as date,remains_question.question_type as questionType,count(remains_question.id) as total FROM remains_question GROUP BY date,remains_question.question_type", nativeQuery = true)
    List<Map<String, Integer>> countByDepartmentId(Long departmentId);

    /**
     * 根据自评项进行问题分布统计
     * @param checkItemId
     * @return
     */
    @Query(value = "SELECT DATE_FORMAT(remains_question.created_time,'%Y-%m-%d') as date,remains_question.question_type as questionType,count(remains_question.id) as total FROM remains_question GROUP BY date,remains_question.question_type", nativeQuery = true)
    List<Map<String, Integer>> countByCheckItemId(Long checkItemId);

    /**
     * 根据组织编号进行整改统计
     * @param departmentId
     * @return
     */
    @Query(value = "SELECT remains_question.question_type AS questionType, count(remains_question.rectification_id != NULL) AS completeQuantity, count(remains_question.rectification_id = NULL) AS uncompleteQuantity FROM remains_question GROUP BY remains_question.question_type", nativeQuery = true)
    List<Map<String, Integer>> countRectification(Long departmentId);

}
