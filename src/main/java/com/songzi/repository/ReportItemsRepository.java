package com.songzi.repository;

import com.songzi.domain.Report;
import com.songzi.domain.ReportItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;


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

    /**
     * 根据当前登录用户统计整体自评结果
     * @param login
     * @return
     */
    @Query(value = "SELECT report_items.jhi_level as level, COUNT(report_items.id) as total FROM report, report_items, jhi_user WHERE report.id = report_items.report_id and report.user_id = jhi_user.id and jhi_user.login = ? GROUP BY report_items.jhi_level", nativeQuery = true)
    List<Object[]> countByUser(String login);

}
