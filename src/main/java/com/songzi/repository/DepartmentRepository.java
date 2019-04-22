package com.songzi.repository;

import com.songzi.domain.Department;
import com.songzi.domain.enumeration.DeleteFlag;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Set;


/**
 * Spring Data JPA repository for the Department entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long>, JpaSpecificationExecutor<Department> {

    List<Department> findAllByParentIdAndDelFlag(Long parentId, DeleteFlag deleteFlag);

    @Query(value = "SELECT d.* FROM department d WHERE d.code IN :codes", nativeQuery = true)
    List<Department> findDepartmentTreeByCodes(@Param("codes") Set<String> codes);

    /**
     * 根据用户ID查部门
     * @param userId
     * @return
     */
    @Query(value = "SELECT d.* from department d LEFT JOIN jhi_user_department jup ON d.id=jup.department_id WHERE jup.user_id = ?1 ", nativeQuery = true)
    Department findOneByUserId(Long userId);

    /**
     * 根据DelFlag查询所有记录
     * @param deleteFlag
     * @return
     */
    List<Department> findAllByDelFlagIs(DeleteFlag deleteFlag);

    /**
     * 根据模糊查询code
     * @param deleteFlag
     * @param code
     * @return
     */
    List<Department> findAllByDelFlagIsAndCodeStartingWith(DeleteFlag deleteFlag, String code);

    /**
     * 查找省份编码
     * @param deleteFlag
     * @param code
     * @return
     */
    @Query(value = "SELECT d.* FROM department d WHERE d.del_flag = ?1 AND d.code <= ?2", nativeQuery = true)
    List<Department> findAllByDelFlagIsAndCodeLessThanEqual(String deleteFlag, int code);

    /**
     * 根据code查询子部门
     * @param deleteFlag
     * @param code
     * @return
     */
    @Query(value = "select d.* from department d where d.del_flag = ?1 and d.code like ?2%", nativeQuery = true)
    List<Department> findAllChildDepartmentByCode(String deleteFlag, String code);



    /**
     * 查询某一级子部门
     *
     * @param deleteFlag
     * @param code
     * @return
     */
    @Query(value = "SELECT d.* FROM department d WHERE d.del_flag = ?1 AND d.code LIKE ?2", nativeQuery = true)
    List<Department> findChildDepartmentByDepartmentCode(String deleteFlag, String code);

    /**
     * 根据部门ID查用户ID
     *
     * @param departmentId
     * @return
     */
    @Query(value = "SELECT user_id from jhi_user_department WHERE department_id = ?1", nativeQuery = true)
    List<Long> findUserIdByDepartmentId(Long departmentId);

    /**
     * 根据用户ID查部门ID
     *
     * @param userId
     * @return
     */
    @Query(value = "SELECT department_id from jhi_user_department WHERE user_id = ?1", nativeQuery = true)
    Long findDepartmentIdByUserId(Long userId);

    /**
     * 根据用户ID更新部门ID
     *
     * @param departmentId
     * @param userId
     * @return
     */
    @Modifying
    @Query(value = "UPDATE jhi_user_department SET department_id = ?1 WHERE user_id = ?2", nativeQuery = true)
    int updateDepartmentIdByUserId(Long departmentId, Long userId);

    /**
     * 插入记录
     * @param userId
     * @param departmentId
     * @return
     */
    @Modifying
    @Query(value = "INSERT INTO jhi_user_department (user_id, department_id) VALUES (:userId,:departmentId)", nativeQuery = true)
    void insertDepartmentIdAndUserId(@Param("userId") Long userId, @Param("departmentId") Long departmentId);

}
