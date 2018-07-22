package com.songzi.repository;

import com.songzi.domain.Department;
import com.songzi.domain.enumeration.DeleteFlag;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the Department entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long>,JpaSpecificationExecutor<Department>  {

    List<Department>  findAllByParentIdAndDelFlag(Long parentId, DeleteFlag deleteFlag);
}
