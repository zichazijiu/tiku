package com.songzi.repository;

import com.songzi.domain.Rectification;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Rectification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RectificationRepository extends JpaRepository<Rectification, Long> {

}
