package com.songzi.repository;

import com.songzi.domain.Report;
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
}
