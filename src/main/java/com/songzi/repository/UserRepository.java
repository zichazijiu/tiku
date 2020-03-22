package com.songzi.repository;

import com.songzi.domain.Department;
import com.songzi.domain.User;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.time.Instant;

/**
 * Spring Data JPA repository for the User entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    String USERS_BY_LOGIN_CACHE = "usersByLogin";

    String USERS_BY_EMAIL_CACHE = "usersByEmail";

    Optional<User> findOneByActivationKey(String activationKey);

    List<User> findAllByActivatedIsFalseAndCreatedDateBefore(Instant dateTime);

    Optional<User> findOneByResetKey(String resetKey);

    Optional<User> findOneByEmailIgnoreCase(String email);

    Optional<User> findOneByLogin(String login);

    Optional<User> findOneByCertDn(String certDn);

    @EntityGraph(attributePaths = {"authorities", "department"})
    Optional<User> findOneWithAuthoritiesById(Long id);

    @EntityGraph(attributePaths = {"authorities", "department"})
    @Cacheable(cacheNames = USERS_BY_LOGIN_CACHE)
    Optional<User> findOneWithAuthoritiesByLogin(String login);

    @EntityGraph(attributePaths = {"authorities", "department"})
    @Cacheable(cacheNames = USERS_BY_EMAIL_CACHE)
    Optional<User> findOneWithAuthoritiesByEmail(String email);

    Page<User> findAllByLoginNot(Pageable pageable, String login);

    Page<User> findAllByLoginNotAndReviewStatusIs(Pageable pageable, String login, String reviewStatus);

    Page<User> findAllByLoginNotAndReviewStatusEndingWith(Pageable pageable, String login, String reviewStatus);

    Page<User> findAllByLoginNotAndReviewStatusIsIn(Pageable pageable, String login, String... reviewStatus);

    Optional<User> findByCertDnIs(String certDn);
    /**
     * 根据某个创建者查出已经激活的用户
     * @param pageable
     * @param login
     * @param isActivated
     * @return
     */
    Page<User> findAllByCreatedByIsAndActivatedIs(Pageable pageable, String login, boolean isActivated);

    /**
     * 根据某个创建者查出用户
     * @param pageable
     * @param login
     * @return
     */
    Page<User> findAllByCreatedByIs(Pageable pageable, String login);

    /**
     * 根据部门查询用户列表
     * @param department
     * @return
     */
    Page<User> findAllByDepartment(Pageable pageable, Department department);

    /**
     * 根据多部门信息查询用户
     * @param pageable
     * @param departments
     * @return
     */
    Page<User> findAllByDepartmentIn(Pageable pageable, List<Department> departments);

    /**
     * 根据部门列表查询用户信息
     * @param departments
     * @return
     */
    List<User> findAllByDepartmentIn(List<Department> departments);
    /**
     * 根据部门查所有用户
     * @param department
     * @return
     */
    List<User> findAllByDepartment(Department department);
}
