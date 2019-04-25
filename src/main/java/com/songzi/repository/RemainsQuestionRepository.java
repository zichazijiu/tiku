package com.songzi.repository;

import com.songzi.domain.RemainsQuestion;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Spring Data JPA repository for the RemainsQuestion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RemainsQuestionRepository extends JpaRepository<RemainsQuestion, Long> {

    /**
     * 根据组织编号进行问题分布统计
     *
     * @param departmentId
     * @return
     */
    @Query(value = "SELECT DATE_FORMAT(remains_question.created_time,'%Y-%m-%d') AS date,remains_question.question_type AS questionType,count(remains_question.id) AS total FROM remains_question, report, report_items, jhi_user_department WHERE remains_question.report_items_id = report_items.id and report.id = report_items.report_id and report.user_id = jhi_user_department.user_id and jhi_user_department.department_id = ?1 GROUP BY date,remains_question.question_type", nativeQuery = true)
    List<Map<String, Object>> countByDepartmentId(Long departmentId);

    /**
     * 根据自评项进行问题分布统计
     *
     * @param checkItemId
     * @return
     */
    @Query(value = "SELECT DATE_FORMAT(remains_question.created_time,'%Y-%m-%d') AS date,remains_question.question_type AS questionType,count(remains_question.id) AS total FROM remains_question, report, report_items WHERE remains_question.report_items_id = report_items.id AND report.id = report_items.report_id AND report_items.check_item_id = :checkItemId AND report.user_id IN :userIds GROUP BY date,remains_question.question_type", nativeQuery = true)
    List<Map<String, Object>> countByCheckItemId(@Param("checkItemId") Long checkItemId, @Param("userIds") Set<Long> userIds);

    /**
     * 根据自评项进行整改统计
     *
     * @param checkItemId
     * @return
     */
    @Query(value = "SELECT remains_question.question_type AS questionType, SUM(if(ISNULL(remains_question.rectification_id) = 0, 1, 0)) AS completeQuantity, SUM(if(ISNULL(remains_question.rectification_id) = 1, 1, 0)) AS uncompleteQuantity FROM remains_question, report, report_items WHERE remains_question.report_items_id = report_items.id AND report.id = report_items.report_id AND report_items.check_item_id = :checkItemId AND report.user_id IN :userIds GROUP BY remains_question.question_type", nativeQuery = true)
    List<Map<String, Object>> countRectification(@Param("checkItemId") Long checkItemId, @Param("userIds") Set<Long> userIds);

}
