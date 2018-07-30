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

    @Query(value = "select * from (select p.id id,p.name, p.description, p.created_date panswer_date, e.id eid, e.score,p.created_date pcreate_date,p.created_by,p.duration,e.status from project p left join examine e on p.id = e.project_id where p.del_flag = ?1 and p.status = ?3 and e.del_flag = ?1 and e.user_id = ?2 and p.name like ?4 and date_format(p.created_date,'%Y-%m-%d') like ?5" +
        " union select p.id id,p.name, p.description, p.created_date panswer_date,null,null,p.created_date pcreate_date,p.created_by,p.duration,null from project p where p.del_flag = ?1 and p.status = ?3 and p.name like ?4 and date_format(p.created_date,'%Y-%m-%d') like ?5 and p.id not in (select e1.project_id from examine e1 left join examiner er on e1.user_id = er.user_id where e1.del_flag = ?1 and e1.user_id = ?2 )) p order by id desc limit ?6,?7",nativeQuery = true)
    List<Object[]> findAllByDelFlagWithExamineAndCreatedDate(String deleteFlag, Long userId, String status, String projectName, String localDate,int start,int size);

    @Query(value = "select count(*) from (select p.id id,p.name, p.description, p.created_date panswer_date, e.id eid, e.score,p.created_date pcreate_date,p.created_by,p.duration,e.status from project p left join examine e on p.id = e.project_id where p.del_flag = ?1 and p.status = ?3 and e.del_flag = ?1 and e.user_id = ?2 and p.name like ?4 and date_format(p.created_date,'%Y-%m-%d') like ?5" +
        " union select p.id id,p.name, p.description, p.created_date panswer_date,null,null,p.created_date pcreate_date,p.created_by,p.duration,null from project p where p.del_flag = ?1 and p.status = ?3 and p.name like ?4 and date_format(p.created_date,'%Y-%m-%d') like ?5 and p.id not in (select e1.project_id from examine e1 left join examiner er on e1.user_id = er.user_id where e1.del_flag = ?1 and e1.user_id = ?2 )) p",nativeQuery = true)
    Long countAllByDelFlagWithExamineAndCreatedDate(String deleteFlag, Long userId, String status, String projectName, String localDate);

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

    @Modifying
    @Query(value = "delete from project_subject where project_id = ?1",nativeQuery = true)
    void deleteProjectSubject(Long projectId);

    List<Project> findAllByDelFlagAndIdNotIn(DeleteFlag deleteFlag,List<Long> ids);
}
