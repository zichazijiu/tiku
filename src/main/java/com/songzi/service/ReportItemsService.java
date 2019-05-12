package com.songzi.service;

import com.songzi.domain.Department;
import com.songzi.domain.Report;
import com.songzi.domain.ReportItems;
import com.songzi.domain.User;
import com.songzi.repository.DepartmentRepository;
import com.songzi.repository.ReportItemsRepository;
import com.songzi.repository.UserRepository;
import com.songzi.security.AuthoritiesConstants;
import com.songzi.security.SecurityUtils;
import com.songzi.web.rest.errors.BadRequestAlertException;
import org.apache.commons.collections4.set.CompositeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing ReportItems.
 */
@Service
@Transactional
public class ReportItemsService {

    private final Logger log = LoggerFactory.getLogger(ReportItemsService.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentSerivce departmentSerivce;

    private final ReportItemsRepository reportItemsRepository;

    public ReportItemsService(ReportItemsRepository reportItemsRepository) {
        this.reportItemsRepository = reportItemsRepository;
    }

    /**
     * Save a reportItems.
     *
     * @param reportItems the entity to save
     * @return the persisted entity
     */
    public ReportItems save(ReportItems reportItems) {
        log.debug("Request to save ReportItems : {}", reportItems);
        return reportItemsRepository.save(reportItems);
    }

    /**
     * Get all the reportItems.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ReportItems> findAll() {
        log.debug("Request to get all ReportItems");
        return reportItemsRepository.findAll();
    }

    /**
     * Get one reportItems by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ReportItems findOne(Long id) {
        log.debug("Request to get ReportItems : {}", id);
        return reportItemsRepository.findOne(id);
    }

    /**
     * Delete the reportItems by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ReportItems : {}", id);
        reportItemsRepository.delete(id);
    }

    /**
     * 根据报告获取报告详情
     *
     * @param report
     * @return
     */
    public List<ReportItems> findAllByReport(Report report) {
        return reportItemsRepository.findAllByReport(report);
    }

    /**
     * 根据当前登录用户统计整体自评结果
     *
     * @param login
     * @return
     */
    public List<Map<String, Object>> countByUser(String login) {
        // 获取登陆用户的信息
        String currentLogin = SecurityUtils.getCurrentUserLogin().get();
        // 获取当前用户的信息
        User user = userRepository.findOneByLogin(currentLogin).get();
        // 当前用户的部门信息
        Department department = user.getDepartment();
        // 查询子部门的用户信息
        List<User> userList = userService.getChildDepartmentUserInfo4Statistic(department);
        // 结果
        List<Map<String, Object>> result;
        if (userList != null && userList.size()>0) {
            // 把自己排除在用户列表中
            userList.removeIf(s -> user.getId().equals(s.getId()));
            // 搜集用户的ID
            Set<Long> userIds = userList.stream().map(x -> x.getId()).collect(Collectors.toSet());
            if (!userIds.isEmpty()) {
                List<Object[]> objects = reportItemsRepository.countByUsers(userIds);
                result = new ArrayList<>(objects.size());
                objects.forEach(obj -> {
                    Map<String, Object> map = new HashMap<>(2);
                    map.put("level", obj[0]);
                    map.put("total", obj[1]);
                    result.add(map);
                });
                return result;
            }
        } else {
            result = new ArrayList<>(1);
            Map<String, Object> map = new HashMap<>(2);
            map.put("level", null);
            map.put("total", 1);
            result.add(map);
        }
        return null;
    }
}
