package com.songzi.repository;

import com.songzi.domain.Rectification;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the Rectification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RectificationRepository extends JpaRepository<Rectification, Long> {

    @Query(value = "select r.* from rectification r left join ( select rectification_id from remains_question where report_items_id = :reportItemId) rq on r.id=rq.rectification_id ", nativeQuery = true)
    List<Rectification> findAllByReportItemId(@Param("reportItemId") Long reportItemId);
}
