package com.songzi.repository;

import com.songzi.domain.Project;
import com.songzi.domain.Subject;
import com.songzi.domain.enumeration.DeleteFlag;
import com.songzi.domain.enumeration.Status;
import com.songzi.service.dto.ProjectDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.time.LocalDate;
import java.util.List;


/**
 * Spring Data JPA repository for the Project entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>,JpaSpecificationExecutor<Project> {


    @Query(value = "select p from Project p where p.delFlag = ?1")
    Page<Project> findAllByDelFlag(DeleteFlag deleteFlag, Pageable pageable);

    @Query(value = "select new com.songzi.service.dto.ProjectDTO(p.id,p.name, p.description, p.createdDate, e.id, e.score,p.createdDate,p.createdBy,p.duration,e.status) from Project p left join Examine e on p.id = e.projectId where p.delFlag = ?1 and p.status = ?3 and (e.delFlag = ?1 or e.delFlag is null) and (e.userId = ?2 or e.userId is null)  and p.name like ?4 and date_format(p.createdDate,'%Y-%m-%d') like ?5")
    Page<ProjectDTO> findAllByDelFlagWithExamineAndCreatedDate(DeleteFlag deleteFlag, Long userId, Status status, String projectName, String localDate, Pageable pageable);

    @Query(value = "select p.name from project p LEFT JOIN project_subject ps on p.id = ps.project_id where ps.subject_id =?1 and p.del_flag = 'NORMAL'",nativeQuery = true)
    List<String> getProjectNameBySujectId(Long subjectId);

    @Modifying
    @Query(value = "INSERT INTO project_subject(project_id,subject_id) VALUES (?1, ?2)",nativeQuery = true)
    void insertProjectSubject(Long projectId,Long subjectId);

    @Query(value = "select count(ps.project_id) from project_subject ps where ps.project_id = ?1 and ps.subject_id = ?2",nativeQuery = true)
    Long getProjectSubject(Long projectId,Long subjectId);

    @Query(value = "select s.id,s.name,s.title,s.description,s.status,s.jhi_type,s.jhi_right,s.created_by,s.created_date from subject s left join project_subject ps on s.id = ps.subject_id where ps.project_id = ?1",nativeQuery = true)
    List<Object[]> findAllSubjectByProjectId(Long projectId);

    @Query(value = "select count(ps.project_id) from project_subject ps inner join subject s on ps.subject_id = s.id where ps.projectId = ?1",nativeQuery = true)
    Long getCountProjecctSubjectByProjectId(Long projectId);

    @Query(value = "delete project_subject ps where ps.project_id = ?1",nativeQuery = true)
    void deleteProjectSubject(Long projectId);
}
