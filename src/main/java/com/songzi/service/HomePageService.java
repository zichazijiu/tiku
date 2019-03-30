package com.songzi.service;

import com.songzi.domain.Authority;
import com.songzi.domain.Department;
import com.songzi.domain.Menu;
import com.songzi.domain.User;
import com.songzi.domain.enumeration.DeleteFlag;
import com.songzi.repository.DepartmentRepository;
import com.songzi.security.AuthoritiesConstants;
import com.songzi.service.dto.CheckItemOverviewDTO;
import com.songzi.service.dto.HomepageDTO;
import com.songzi.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Ke Qingyuan on 2019/3/4.
 */
@Service
@Transactional
public class HomePageService {

    private final Logger log = LoggerFactory.getLogger(HomePageService.class);

    @Autowired
    private UserService userService;

    @Autowired
    private DepartmentSerivce departmentSerivce;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private MenuService menuService;

    public HomepageDTO getUserHomepage(String login) {
        log.debug("GET REQUEST HOMEPAGE user: {}", login);
        HomepageDTO homepageDTO = new HomepageDTO();
        User user = userService.getUserWithAuthoritiesByLogin(login).get();
        List<Menu> menuList = new ArrayList<>();
        Set<String> roles = new HashSet<>();
        if (user.getAuthorities() != null) {
            Iterator<Authority> authorityIterator = user.getAuthorities().iterator();
            // 遍历角色
            while (authorityIterator.hasNext()) {
                // 菜单权限获取权限
                String role = authorityIterator.next().getName();
                roles.add(role);
                menuList.addAll(menuService.getMenuListByAuthority(role));

            }
            if (menuList != null && menuList.size() > 0) {
                Map<Long, Menu> menuMap = menuList.stream().collect(Collectors.toMap(Menu::getId, menu -> menu, (k1, k2) -> k1));
                // 菜单
                homepageDTO.setMenuList(menuMap.values().stream().collect(Collectors.toList()));
            } else {
                homepageDTO.setMenuList(menuList);
            }

            // 封装部门
            List<Department> departmentList;
            if (roles.contains(AuthoritiesConstants.ADMIN) || roles.contains(AuthoritiesConstants.BU_ADMIN)) {
                // 管理员、部级管理员展现一级部门
                departmentList = departmentRepository.findChildDepartmentByDepartmentCode(DeleteFlag.NORMAL.name(),"8602__");
            } else if (roles.contains(AuthoritiesConstants.TING_ADMIN)
                || roles.contains(AuthoritiesConstants.JU_ADMIN)) {
                // 厅、局
                String code = user.getDepartment().getCode();
                String childCode = code.substring(0, code.length() - 3);
                // 查看该机构下的所有子部门
                departmentList = departmentRepository.findAllByDelFlagIsAndCodeStartingWith(DeleteFlag.NORMAL, childCode);
            } else if (roles.contains(AuthoritiesConstants.CHU_ADMIN)) {
                // 处
                String code = user.getDepartment().getCode();
                String childCode = code.substring(0, code.length() - 2);
                // 查看该机构下的所有子部门
                departmentList = departmentRepository.findAllByDelFlagIsAndCodeStartingWith(DeleteFlag.NORMAL, childCode);
            } else { // 普通用户
                departmentList = departmentSerivce.getDepartmentTreeByUserId(user.getId());
            }

            // 部门树
            homepageDTO.setDepartmentList(departmentList);

        }
        return homepageDTO;
    }

    /**
     * 查询部门的提报信息
     * @param departmentId
     * @return
     */
    public CheckItemOverviewDTO getReportOverview(Long departmentId) {
        Department department = departmentRepository.findOne(departmentId);
        if (department == null)
            throw new BadRequestAlertException("部门不存在", this.getClass().getName(), "部门不存在");
        CheckItemOverviewDTO result = new CheckItemOverviewDTO();
        result.setDeptName(department.getName()); // 部门名称

        return result;

    }
}
