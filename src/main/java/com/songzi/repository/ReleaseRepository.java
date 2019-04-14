package com.songzi.repository;

import com.songzi.domain.Release;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Release entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReleaseRepository extends JpaRepository<Release, Long> {

}
