package com.songzi.service;

import com.songzi.domain.*;
import com.songzi.domain.enumeration.AuthoritiesType;
import com.songzi.domain.enumeration.DeleteFlag;
import com.songzi.domain.enumeration.ReportStatus;
import com.songzi.repository.CheckItemRepository;
import com.songzi.repository.DepartmentRepository;
import com.songzi.repository.ReportItemsRepository;
import com.songzi.repository.ReportRepository;
import com.songzi.security.AuthoritiesConstants;
import com.songzi.service.dto.ReportOverviewDTO;
import liquibase.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Report.
 */
@Service
@Transactional
public class ReportService {

    private final Logger log = LoggerFactory.getLogger(ReportService.class);

    @Autowired
    private UserService userService;

    @Autowired
    private CheckItemService checkItemService;

    @Autowired
    private ReportItemsRepository reportItemsRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private CheckItemRepository checkItemRepository;

    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    /**
     * Save a report.
     *
     * @param report the entity to save
     * @return the persisted entity
     */
    public Report save(Report report) {
        log.debug("Request to save Report : {}", report);
        return reportRepository.save(report);
    }

    /**
     * Get all the reports.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Report> findAll() {
        log.debug("Request to get all Reports");
        return reportRepository.findAll();
    }

    /**
     * Get one report by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Report findOne(Long id) {
        log.debug("Request to get Report : {}", id);
        return reportRepository.findOne(id);
    }

    /**
     * Delete the report by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Report : {}", id);
        reportRepository.delete(id);
    }

    /**
     * 获取用户的提报信息
     *
     * @param login
     * @return
     */
    public Report getUserReport(String login) {
        if (StringUtils.isNotEmpty(login)) {
            User user = userService.getUserWithAuthoritiesByLogin(login).get();
            List<Report> reportList = reportRepository.findByUserId(user.getId());
            // 用户没有报告，生成一份报告
            if (reportList == null || reportList.size() < 1) {
                Report report = new Report();
                // 设置用户
                report.setUser(user);
                // 设置时间
                report.setCreatedTime(ZonedDateTime.now());
                // 设置状态
                report.setReportStatus(ReportStatus.NEW);
                // 保存报告
                reportRepository.save(report);
                // 提报的自评项目入库
                List<CheckItem> checkItems = checkItemService.findAllByUser(login);
                checkItems.stream().map(item -> {
                    ReportItems obj = new ReportItems();
                    obj.setReport(report);
                    obj.setCheckItem(item);
                    // 保存提报信息
                    reportItemsRepository.save(obj);
                    return obj;
                }).collect(Collectors.toSet());

                return report;
            }

            return reportList.get(0);

        }
        return null;
    }

    /**
     * 更新报告详情
     *
     * @param reportId
     * @param reportItemsList
     * @return
     */
    public Report updateReportItems(Long reportId, List<ReportItems> reportItemsList) {
        // 获取报告
        Report report = reportRepository.findOne(reportId);
        if (report != null && reportItemsList != null && reportItemsList.size() > 0) {
            // 更新报告详情
            reportItemsList.forEach(reportItems -> {
                // 更新报告条目
                if (reportItems.getId() != null) {
                    reportItems.setReport(report);
                    reportItemsRepository.save(reportItems);
                }
            });
            // 更新报告
            report.setReportStatus(ReportStatus.FINISH);
            reportRepository.save(report);
        }
        return report;
    }

    /**
     * 获取用户的提报概览信息
     *
     * @param login
     * @return
     */
    public List<ReportOverviewDTO> getUserReportOverview(String login) {
        Report report = getUserReport(login);
        List<Object[]> objectList = reportRepository.findAllReportOverview(report.getId());
        List<ReportOverviewDTO> reportOverviewDTOList = objectList
            .stream()
            .map(objects -> {
                LocalDate reportCreatedTime = report.getCreatedTime().toLocalDate();
                String reportUsername = report.getUser().getFirstName();
                Long reportId = objects[0] == null ? report.getId() : ((BigInteger) objects[0]).longValue();
                String checkItemContent = objects[1] == null ? "" : (String) objects[1];
                LocalDate checkItemCreatedTime = objects[2] == null ? null : ((Timestamp) objects[2]).toLocalDateTime().toLocalDate();
                LocalDate rectificationTiem = objects[3] == null ? null : ((Timestamp) objects[3]).toLocalDateTime().toLocalDate();
                String measure = objects[4] == null ? "" : (String) objects[4];
                String result = objects[5] == null ? "" : (String) objects[5];
                return new ReportOverviewDTO(reportCreatedTime, reportUsername, reportId, checkItemContent,
                    checkItemCreatedTime, rectificationTiem, measure, result);
            })
            .collect(Collectors.toList());
        return reportOverviewDTOList;
    }

    /**
     * 检查部门报告
     *
     * @param deptId
     * @param reportId
     * @param reportItemsList
     */
    public void checkReport(Long deptId, Long reportId, List<ReportItems> reportItemsList) {
        // 根据报告获取用户信息
        User reportUser = findOne(reportId).getUser();
        Set<String> authorities = reportUser.getAuthorities().stream().map(auth -> auth.getName()).collect(Collectors.toSet());
        // 管理员需要检查提报信息
        if (authorities.contains(AuthoritiesConstants.ADMIN)
            || authorities.contains(AuthoritiesConstants.BU_ADMIN)
            || authorities.contains(AuthoritiesConstants.TING_ADMIN)
            || authorities.contains(AuthoritiesConstants.JU_ADMIN)
            || authorities.contains(AuthoritiesConstants.CHU_ADMIN)) {
            // 获取部门
            Department department = departmentRepository.findOne(deptId);
            // 获取子部门
            List<Department> childDepartmentList = departmentRepository.findFirstLevelChildDepartmentByDepartmentCode(DeleteFlag.NORMAL.name(), department.getCode());
            // 遍历每个子部门提交的报告信息
            childDepartmentList.forEach(dept -> {
                // 获取该部门的报告
                List<ReportItems> reportItems = reportItemsRepository.findAllByDepartmentId(dept.getId());
                // 遍历报告详情检查是否有"C"
                List<ReportItems> reportItemsC = reportItems.stream()
                    .filter(item -> "C".equals(item.getLevel()))
                    .collect(Collectors.toList());
                reportItemsC.forEach(System.out::println);
                // 遍历C的信息汇总
//                Map<Long,CheckItem> checkItemMap = new HashMap<>(16);
//                reportItemsC.forEach(item->{
//                    CheckItem checkItem = checkItemRepository.findOne(item.get)
//                });

            });
        }
    }
}
