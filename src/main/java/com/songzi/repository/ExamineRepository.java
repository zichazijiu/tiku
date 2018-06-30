package com.songzi.repository;

import com.songzi.domain.Examine;
import com.songzi.domain.enumeration.DeleteFlag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Examine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExamineRepository extends JpaRepository<Examine, Long>,JpaSpecificationExecutor<Examine> {

    @Query(value = "select e.id from Examine e where e.userId = ?1")
    Long findIdByUserId(Long userId);

    Page<Examine> findAllByDelFlag(DeleteFlag del, Pageable pageable);
}
