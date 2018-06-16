package com.songzi.repository;

import com.songzi.domain.Project;
import com.songzi.domain.enumeration.DeleteFlag;
import com.songzi.service.dto.ProjectDTO;
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

    @Query(value = "select new com.songzi.service.dto.ProjectDTO(p.name, p.description, p.createdDate, e.id, e.score) from Project p left join p.examines e where p.delFlag = ?1 and e.delFlag = ?1 or e.delFlag IS NULL")
    Page<ProjectDTO> findAllByDelFlagWithExamine(DeleteFlag deleteFlag, Pageable pageable);
}
