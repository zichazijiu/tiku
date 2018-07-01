package com.songzi.repository;

import com.songzi.domain.Subject;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the Subject entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> ,JpaSpecificationExecutor<Subject>{

    @Query(value = "select s.* from subject s left join project_subject ps on s.id = ps.subject_id where s.del_flag = 'NORMAL' and ps.project_id = ?1 order by s.id",nativeQuery = true)
    List<Subject> findAllByProjectId(Long projectId);
}
