package com.songzi.repository;

import com.songzi.domain.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data JPA repository for the Statistics entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StatisticsRepository extends JpaRepository<Statistics, Long> {

    @Query(value = "SELECT FORMAT(AVG(e.score),0) as score,DATE_FORMAT(e.created_date,'%m') as month,DATE_FORMAT(e.created_date,'%Y-%m')  FROM `examine` e where e.user_id =?1 GROUP BY DATE_FORMAT(e.created_date,'%Y-%m')",nativeQuery = true)
    List<Object[]> getStatisticsCurrentUser(Long userId);



    @Query(value = "SELECT FORMAT(AVG(e.score),0) as score,DATE_FORMAT(e.created_date,'%m') as month,DATE_FORMAT(e.created_date,'%Y-%m') FROM `examine` e where e.department_id =?1 GROUP BY DATE_FORMAT(e.created_date,'%Y-%m')",nativeQuery = true)
    List<Object[]> getStatisticsDepartment(Long departmentId);


    @Query(value = "select count(e.score),e.department_id from examine e where DATE_FORMAT(e.created_date,'%Y-%m') = ?1 GROUP BY e.department_id order by count(e.score) desc",nativeQuery = true)
    List<Object[]> getStatisticsSortDepartment(String compareTime);
}
