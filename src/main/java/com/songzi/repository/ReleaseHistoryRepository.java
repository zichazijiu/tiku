package com.songzi.repository;

import com.songzi.domain.ReleaseHistory;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the ReleaseHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReleaseHistoryRepository extends JpaRepository<ReleaseHistory, Long> {

    @Query(value = "select release_view.release_history_id,release_view.release_id,release_view.user_id,release_view.login,department_view.id as department_id,department_view.code,department_view.name " +
        "from (select check_item_release_history.id as release_history_id,release_id,jhi_user.id as user_id,jhi_user.login as login from check_item_release_history JOIN jhi_user on jhi_user.login = check_item_release_history.created_by) release_view left join (select * from department join jhi_user_department on department.id=jhi_user_department.department_id) department_view on release_view.user_id = department_view.user_id WHERE code like :code", nativeQuery = true)
    List<Object[]> findAllByDepartmentCode(@Param("code") String code);
}
