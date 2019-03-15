package com.songzi.repository;

import com.songzi.domain.Rectification;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Rectification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RectificationRepository extends JpaRepository<Rectification, Long> {

}
