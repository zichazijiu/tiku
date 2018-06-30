package com.songzi.repository;

import com.songzi.domain.Project;
import com.songzi.domain.enumeration.DeleteFlag;
import com.songzi.service.dto.ProjectDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the Project entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>,JpaSpecificationExecutor<Project> {


    @Query(value = "select p from Project p where p.delFlag = ?1")
    Page<Project> findAllByDelFlag(DeleteFlag deleteFlag, Pageable pageable);

    @Query(value = "select new com.songzi.service.dto.ProjectDTO(p.name, p.description, p.createdDate, e.id, e.score) from Project p left join Examine e on p.id = e.projectId where p.delFlag = ?1 and e.delFlag = ?1 and e.userId = ?2")
    Page<ProjectDTO> findAllByDelFlagWithExamine(DeleteFlag deleteFlag,Long userId, Pageable pageable);

    @Query(value = "select p.name from project p LEFT JOIN project_subject ps on p.id = ps.project_id where ps.subject_id =?1 and p.del_flag = 'NORMAL'",nativeQuery = true)
    List<String> getProjectNameBySujectId(Long subjectId);
}
