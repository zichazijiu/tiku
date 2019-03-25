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

    @Query(value = "SELECT reportItems.reportId , reportItems.checkItemContent , reportItems.checkItemCreatedTime , remainsQuestion.rectificationTime , remainsQuestion.measure , remainsQuestion.result FROM( SELECT a.id , a.report_id AS reportId , b.content AS checkItemContent , b.created_date AS checkItemCreatedTime FROM report_items a LEFT JOIN check_item b ON a.check_item_id = b.id) reportItems LEFT JOIN( SELECT a.report_items_id , b.rectification_time AS rectificationTime , b.measure , b.result FROM remains_question a JOIN rectification b ON a.rectification_id = b.id) AS remainsQuestion ON reportItems.id = remainsQuestion.report_items_id WHERE reportItems.reportId = ?1", nativeQuery = true)
    List<Object[]> findAllReportOverview(Long reportId);

    @Query(value = "SELECT report.* FROM report report LEFT JOIN jhi_user_department department ON report.user_id = department.user_id WHERE department.department_id = ?1", nativeQuery = true)
    List<Report> findAllByDepartmentId(Long deptId);

}
