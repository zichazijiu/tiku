package com.songzi.repository;

import com.songzi.domain.Project;
import com.songzi.domain.enumeration.DeleteFlag;
import com.songzi.service.dto.ProjectDTO;
import com.songzi.web.rest.vm.ProjectVM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Project entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {


    @Query(value = "select p from Project p where p.delFlag = ?1")
    Page<Project> findAllByDelFlag(DeleteFlag deleteFlag, Pageable pageable);

//    @Query(value = "select p from Project p left join Examine e where", nativeQuery = true)
//    Page<ProjectDTO> findAllByDelFlagWithExamine();
}
