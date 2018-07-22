package com.songzi.repository;

import com.songzi.domain.Examiner;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the Examiner entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExaminerRepository extends JpaRepository<Examiner, Long>,JpaSpecificationExecutor<Examiner>{

    Examiner findOneByUserId(Long userId);

    @Query(value = "select u.id as uid,u.login,u.password_hash,e.id as eid,e.name,e.department_id,e.cell_phone,e.email,e.sex,e.birth,e.location,e.phone,e.address from examiner e LEFT JOIN jhi_user u on e.user_id = u.id",nativeQuery = true)
    List<Object[]> exprotExaminerAndUserMessage();

    List<Examiner> findAllByDepartmentId(Long departmentId);
}
