package com.songzi.repository;

import com.songzi.domain.Report;
import com.songzi.service.dto.ReportOverviewDTO;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Report entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    @Query("select report from Report report where report.user.login = ?#{principal.username}")
    List<Report> findByUserIsCurrentUser();

    @Query(value = "select report.* from report report where report.user_id = ?1", nativeQuery = true)
    List<Report> findByUserId(Long userId);

    @Query(value = "SELECT reportItems.reportId , reportItems.checkItemContent , reportItems.checkItemCreatedTime , remainsQuestion.rectificationTime , remainsQuestion.measure , remainsQuestion.result , reportItems.reportItemId, reportItems.reportItemLevel FROM( SELECT a.id AS reportItemId , a.report_id AS reportId , a.jhi_level AS reportItemLevel, b.content AS checkItemContent , b.created_date AS checkItemCreatedTime , b.id AS checkItemId FROM report_items a LEFT JOIN check_item b ON a.check_item_id = b.id WHERE a.report_id = ?1) reportItems LEFT JOIN( SELECT a.report_items_id , b.rectification_time AS rectificationTime , b.measure , b.result FROM remains_question a RIGHT JOIN rectification b ON a.rectification_id = b.id) AS remainsQuestion ON reportItems.reportItemId = remainsQuestion.report_items_id ORDER BY reportItemId ASC", nativeQuery = true)
    List<Object[]> findAllReportOverview(Long reportId);

    @Query(value = "SELECT report.* FROM report report LEFT JOIN jhi_user_department department ON report.user_id = department.user_id WHERE department.department_id = ?1", nativeQuery = true)
    List<Report> findAllByDepartmentId(Long deptId);

}
