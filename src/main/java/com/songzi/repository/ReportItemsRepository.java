package com.songzi.repository;

import com.songzi.domain.Report;
import com.songzi.domain.ReportItems;
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
}
