package com.songzi.service;

import com.songzi.domain.Department;
import com.songzi.domain.RemainsQuestion;
import com.songzi.domain.User;
import com.songzi.repository.RemainsQuestionRepository;
import com.songzi.repository.UserRepository;
import com.songzi.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing RemainsQuestion.
 */
@Service
@Transactional
public class RemainsQuestionService {

    private final Logger log = LoggerFactory.getLogger(RemainsQuestionService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentService departmentSerivce;

    @Autowired UserService userService;

    private final RemainsQuestionRepository remainsQuestionRepository;

    public RemainsQuestionService(RemainsQuestionRepository remainsQuestionRepository) {
        this.remainsQuestionRepository = remainsQuestionRepository;
    }

    /**
     * Save a remainsQuestion.
     *
     * @param remainsQuestion the entity to save
     * @return the persisted entity
     */
    public RemainsQuestion save(RemainsQuestion remainsQuestion) {
        log.debug("Request to save RemainsQuestion : {}", remainsQuestion);
        return remainsQuestionRepository.save(remainsQuestion);
    }

    /**
     * Get all the remainsQuestions.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<RemainsQuestion> findAll() {
        log.debug("Request to get all RemainsQuestions");
        return remainsQuestionRepository.findAll();
    }

    /**
     * Get one remainsQuestion by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public RemainsQuestion findOne(Long id) {
        log.debug("Request to get RemainsQuestion : {}", id);
        return remainsQuestionRepository.findOne(id);
    }

    /**
     * Delete the remainsQuestion by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete RemainsQuestion : {}", id);
        remainsQuestionRepository.delete(id);
    }

    /**
     * 根据组织编号进行问题分布统计
     *
     * @param departmentId
     * @returnC Map<String,Integer>
     */
    public List<Map<String, Object>> countByDepartmentId(Long departmentId) {
        log.debug("Request to count RemainsQuestion by departmentId : {}", departmentId);
        return remainsQuestionRepository.countByDepartmentId(departmentId);
    }

    /**
     * 根据自评项进行问题分布统计
     *
     * @param checkItemId
     * @return Map<String,Integer>
     */
    public List<Map<String, Object>> countByCheckItemId(Long checkItemId) {
        log.debug("Request to count RemainsQuestion by checkItemId : {}", checkItemId);
        // 获取登陆用户信息
        User user = SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin).get();
        // 获取登陆用户的部门信息
        Department department = user.getDepartment();
        // 获取登陆用户的子部门信息的用户信息
        List<User> userList = userService.getChildDepartmentUserInfo4Statistic(department);
        // 把自己排除在用户列表中
        if (userList!=null&&userList.size()>0){
            userList.removeIf(s -> user.getId().equals(s.getId()));
        }
        Set<Long> userIds = userList.stream().map(x -> x.getId()).collect(Collectors.toSet());
        if (userIds.isEmpty()){
            return null;
        }
        return remainsQuestionRepository.countByCheckItemId(checkItemId, userIds);
    }

    /**
     * 根据自评项进行整改统计
     *
     * @param checkItemId
     * @return
     */
    public List<Map<String, Object>> countRectification(Long checkItemId) {
        log.debug("Request to count Rectification by checkItemId : {}", checkItemId);
        // 获取登陆用户信息
        User user = SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin).get();
        // 获取登陆用户的部门信息
        Department department = user.getDepartment();
        // 获取登陆用户的子部门信息的用户信息
        List<User> userList = userService.getChildDepartmentUserInfo4Statistic(department);
        // 把自己排除在用户列表中
        if (userList!=null&userList.size()>0){
            userList.removeIf(s -> user.getId().equals(s.getId()));
        }
        Set<Long> userIds = userList.stream().map(x -> x.getId()).collect(Collectors.toSet());
        if (userIds.isEmpty()){
            return null;
        }
        return remainsQuestionRepository.countRectification(checkItemId, userIds);
    }

}
