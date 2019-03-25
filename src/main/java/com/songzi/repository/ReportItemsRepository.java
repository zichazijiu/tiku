package com.songzi.repository;

import com.songzi.domain.Report;
import com.songzi.domain.ReportItems;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the ReportItems entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportItemsRepository extends JpaRepository<ReportItems, Long> {

    List<ReportItems> findAllByReport(Report report);

    /**
     * 根据部门ID获取报告详情
     *
     * @param departmentId
     * @return
     */
    @Query(value = "SELECT * FROM report_items reportItems WHERE reportItems.report_id IN (SELECT report.id FROM report report LEFT JOIN jhi_user_department department ON report.user_id = department.user_id WHERE department.department_id = ?1)", nativeQuery = true)
    List<ReportItems> findAllByDepartmentId(Long departmentId);
}
