package com.songzi.repository;

import com.songzi.domain.Statistics;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Statistics entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StatisticsRepository extends JpaRepository<Statistics, Long> {

}
