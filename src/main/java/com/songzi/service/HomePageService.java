package com.songzi.service;

import com.songzi.domain.*;
import com.songzi.domain.enumeration.DeleteFlag;
import com.songzi.repository.*;
import com.songzi.security.AuthoritiesConstants;
import com.songzi.security.SecurityUtils;
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
    private UserRepository userRepository;

    @Autowired
    private DepartmentSerivce departmentSerivce;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ReportItemsRepository reportItemsRepository;

    @Autowired
    private RectificationRepository rectificationRepository;


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
            List<Department> departmentList = null;
            if (roles.contains(AuthoritiesConstants.ADMIN) || roles.contains(AuthoritiesConstants.BU_ADMIN)) {
                // 管理员、部级管理员展现一级部门
                departmentList = departmentRepository.findChildDepartmentByDepartmentCode(DeleteFlag.NORMAL.name(), "8602__");
            } else if (roles.contains(AuthoritiesConstants.TING_ADMIN)) {
                // 厅
                String code = user.getDepartment().getCode();
                departmentList = getHomePageDepartmentListByCode(code, 6);

            } else if (roles.contains(AuthoritiesConstants.JU_ADMIN)) {
                // 局
                String code = user.getDepartment().getCode();
                departmentList = getHomePageDepartmentListByCode(code, 8);
            } else if (roles.contains(AuthoritiesConstants.CHU_ADMIN)) {
                // 处
                String code = user.getDepartment().getCode();
                departmentList = getHomePageDepartmentListByCode(code, 10);
            } else { // 普通用户
                List<Department> allDepartmentList = departmentSerivce.getDepartmentTreeByUserId(user.getId());
                // FIXME：新的需求是只给最后两级别的树
                departmentList = allDepartmentList.subList(allDepartmentList.size() - 2, allDepartmentList.size());
            }

            // 部门树
            homepageDTO.setDepartmentList(departmentList);

        }
        return homepageDTO;
    }

    /**
     * @param code
     * @param except
     * @return
     */
    private List<Department> getHomePageDepartmentListByCode(String code, int except) {

        List<Department> departmentList = new ArrayList<>();
        if (code.length() == except) {
            //
            String prefix = "__";
            // 查出二级
            List<Department> deptLevel2 = departmentRepository.findChildDepartmentByDepartmentCode(DeleteFlag.NORMAL.toString(), code + prefix);
            departmentList.addAll(deptLevel2);
            // 查出三级
            List<Department> deptLevel3 = departmentRepository.findChildDepartmentByDepartmentCode(DeleteFlag.NORMAL.toString(), code + prefix + prefix);
            departmentList.addAll(deptLevel3);
        } else {
            StringBuilder paramCode = new StringBuilder(code);
            paramCode.replace(code.length() - 3, code.length(), "___");
            departmentList = departmentRepository.findChildDepartmentByDepartmentCode(DeleteFlag.NORMAL.toString(), paramCode.toString());
        }
        return departmentList;
    }

    /**
     * 查询部门的提报信息
     *
     * @param departmentId
     * @return
     */
    public CheckItemOverviewDTO getReportOverview(Long departmentId) {
        Department department = departmentRepository.findOne(departmentId);
        if (department == null) {
            throw new BadRequestAlertException("部门不存在", this.getClass().getName(), "部门不存在");
        }
        // 根据部门展现报告
        CheckItemOverviewDTO result = new CheckItemOverviewDTO();
        // 部门名称
        result.setDeptName(department.getName());
        // 当前用户是普通用户，展现用户自己的报告
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.USER)) {
            List<Report> reports = reportRepository.findByUserIsCurrentUser();
            if (reports != null && reports.size() > 0) {
                Report report = reports.get(0);
                User user = userRepository.findOne(report.getUser().getId());
                // 提报人
                result.setCreatedUser(user.getLastName() + user.getFirstName());
                // 提报时间
                result.setCreatedDate(report.getCreatedTime().toLocalDate().toString());
                // 总的考评结果
                result.setCheckResult(report.getLevel() == null ? "" : report.getLevel());
                List<ReportItems> reportItemsList = reportItemsRepository.findAllByReport(report);
                result.setReportItemsList(reportItemsList);
                // 自查项目
//                List<String> checkDescriptionList = new ArrayList<>(reportItemsList.size());
                // 整改结果
//                List<Rectification> rectificationList = new ArrayList<>(16);
//                reportItemsList.forEach(item -> {
//                    rectificationList.addAll(rectificationRepository.findAllByReportItemId(item.getId()));
//                    String level = item.getLevel() == null ? "" : item.getLevel();
//                    checkDescriptionList.add(item.getCheckItem().getContent() + "$$$" + level);
//                });

                // 更改
//                result.setRectificationList(rectificationList);
                // 整改信息
//                result.setCheckDescription(checkDescriptionList);
            }
        } else {
            // 根据部门查出用户
            List<User> userList = userRepository.findAllByDepartment(department);
            if (userList != null && userList.size() > 0) {
                User user = userList.get(0);
                List<Report> reports = reportRepository.findByUserId(user.getId());
                if (reports != null && reports.size() > 0) {
                    Report report = reports.get(0);
                    // 提报人
                    result.setCreatedUser(user.getLastName() + user.getFirstName());
                    // 提报时间
                    result.setCreatedDate(report.getCreatedTime().toLocalDate().toString());
                    // 总的考评结果
                    result.setCheckResult(report.getLevel() == null ? "" : report.getLevel());
                    List<ReportItems> reportItemsList = reportItemsRepository.findAllByReport(report);
                    result.setReportItemsList(reportItemsList);
                    // 自查项目
//                    List<String> checkDescriptionList = new ArrayList<>(reportItemsList.size());
                    // 整改结果
//                    List<Rectification> rectificationList = new ArrayList<>(16);
//                    reportItemsList.forEach(item -> {
//                        rectificationList.addAll(rectificationRepository.findAllByReportItemId(item.getId()));
//                        String level = item.getLevel() == null ? "" : item.getLevel();
//                        checkDescriptionList.add(item.getCheckItem().getContent() + "$$$" + level);
//                    });

                    // 更改
//                    result.setRectificationList(rectificationList);
                    // 整改信息
//                    result.setCheckDescription(checkDescriptionList);
                }
            }
        }
        return result;
    }

//  public static void main(String[] gras) {
//        List<String> a = new ArrayList<>();
//        a.add("1");
//        a.add("2");
//        a.add("3");
//        List<String> b = a.subList(a.size()-2,a.size());
//        System.out.print(Arrays.toString(b.toArray()));
//  }
}
