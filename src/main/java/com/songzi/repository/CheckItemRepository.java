package com.songzi.repository;

import com.songzi.domain.CheckItem;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the CheckItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CheckItemRepository extends JpaRepository<CheckItem, Long> {

}
