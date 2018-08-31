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

    @Query(value = "SELECT DATE_FORMAT(e.created_date,'%Y-%m') as month, FORMAT(AVG(e.score),1) as score  FROM `examine` e where e.user_id =?1 and DATE_FORMAT(e.created_date, '%Y') = ?2 GROUP BY month", nativeQuery = true)
    List<Object[]> getStatisticsCurrentUser(Long userId, String year);


    @Query(value = "SELECT FORMAT(AVG(e.score),0) as score,DATE_FORMAT(e.created_date,'%m') as month,DATE_FORMAT(e.created_date,'%Y-%m') FROM `examine` e where e.department_id =?1 GROUP BY DATE_FORMAT(e.created_date,'%Y-%m')", nativeQuery = true)
    List<Object[]> getStatisticsDepartment(Long departmentId);


    @Query(value = "select count(e.score),e.department_id from examine e where DATE_FORMAT(e.created_date,'%Y-%m') = ?1 GROUP BY e.department_id order by count(e.score) desc", nativeQuery = true)
    List<Object[]> getStatisticsSortDepartment(String compareTime);

    @Query(value = "SELECT d.name, FORMAT(AVG(e.score), 1) as score FROM `examine` e left join `department` d on e.department_id = d.id WHERE DATE_FORMAT(e.created_date, '%Y-%m') = ?1  and e.del_flag = 'NORMAL' group by e.department_id order by score DESC LIMIT 0,10", nativeQuery = true)
    List<Object[]> statisticsDepartmentAVGScoreTop10ByCreatedDateAndDelFlagOrderByDESC(String createDate);

    @Query(value = "SELECT d.name, FORMAT(AVG(e.score), 1) as score FROM `examine` e left join `department` d on e.department_id = d.id WHERE DATE_FORMAT(e.created_date, '%Y-%m') = ?1  and e.del_flag = 'NORMAL' group by e.department_id order by score ASC LIMIT 0,10", nativeQuery = true)
    List<Object[]> statisticsDepartmentAVGScoreTop10ByCreatedDateAndDelFlagOrderByASC(String createDate);

    /**
     * 根据部门和创建时间统计该部门近6个月的平均分
     * @param departmentId
     * @param startDate
     * @param endDate
     * @return
     */
    @Query(value = "SELECT DATE_FORMAT(e.created_date, '%Y-%m') as date, FORMAT(AVG(e.score), 1) as score FROM `examine` e where e.department_id = ?1  and e.del_flag = 'NORMAL'  and (DATE_FORMAT(e.created_date, '%Y-%m') between ?2 and ?3) group by date;", nativeQuery = true)
    List<Object[]> statistics6MonthAVGScoreByDepartmentAndCreatedDate(Long departmentId, String startDate, String endDate);
}
