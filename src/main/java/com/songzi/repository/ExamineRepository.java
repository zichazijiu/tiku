package com.songzi.repository;

import com.songzi.domain.Examine;
import com.songzi.domain.enumeration.DeleteFlag;
import com.songzi.domain.enumeration.ExamineStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the Examine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExamineRepository extends JpaRepository<Examine, Long>, JpaSpecificationExecutor<Examine> {

    @Query(value = "select e.id from Examine e where e.userId = ?1")
    Long findIdByUserId(Long userId);

    Page<Examine> findAllByDelFlag(DeleteFlag del, Pageable pageable);

    List<Examine> findAllByProjectIdAndDelFlagAndUserId(Long projectId, DeleteFlag deleteFlag, Long userid);

    List<Examine> findAllByStatusAndDelFlag(ExamineStatus examineStatus, DeleteFlag deleteFlag);

    List<Examine> findAllByDelFlagAndUserId(DeleteFlag deleteFlag, Long useId);

    @Query(value = "SELECT sum(t.actual_duration) FROM examine t where DATE_FORMAT(t.created_date,'%Y-%m') = ?1 and t.del_flag = '0'", nativeQuery = true)
    Long getTotalTime(String yearMonth);

    @Query(value = "SELECT sum(t.actual_duration) FROM examine t where DATE_FORMAT(t.created_date,'%Y-%m') = ?1 and t.del_flag = '0' and t.actual_duration <= '1800'", nativeQuery = true)
    Long getTotalTime30(String yearMonth);

    @Query(value = "SELECT sum(t.actual_duration) FROM examine t where DATE_FORMAT(t.created_date,'%Y-%m') = ?1 and t.del_flag = '0' and t.actual_duration > '1800' and t.actual_duration <= '3000'", nativeQuery = true)
    Long getTotalTime30_50(String yearMonth);

    @Query(value = "SELECT sum(t.actual_duration) FROM examine t where DATE_FORMAT(t.created_date,'%Y-%m') = ?1 and t.del_flag = '0' and t.actual_duration > '3000'", nativeQuery = true)
    Long getTotalTime50_(String yearMonth);

}
