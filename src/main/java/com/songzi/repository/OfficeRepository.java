package com.songzi.repository;

import com.songzi.domain.Office;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Office entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OfficeRepository extends JpaRepository<Office, Long> {

}
