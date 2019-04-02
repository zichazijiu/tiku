package com.songzi.service;

import com.songzi.domain.*;
import com.songzi.domain.enumeration.DeleteFlag;
import com.songzi.domain.enumeration.ReportStatus;
import com.songzi.repository.CheckItemRepository;
import com.songzi.repository.DepartmentRepository;
import com.songzi.repository.ReportItemsRepository;
import com.songzi.repository.ReportRepository;
import com.songzi.security.AuthoritiesConstants;
import com.songzi.service.dto.ReportOverviewDTO;
import com.songzi.web.rest.errors.BadRequestAlertException;
import com.songzi.web.rest.vm.ReportDetailVM;
import liquibase.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;
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
        Report report = null;
        if (StringUtils.isNotEmpty(login)) {
            User user = userService.getUserWithAuthoritiesByLogin(login).get();
            List<Report> reportList = reportRepository.findByUserId(user.getId());
            // 用户没有报告，生成一份报告
            if (reportList == null || reportList.size() < 1) {
                report = new Report();
                // 设置用户
                report.setUser(user);
                // 设置时间
                report.setCreatedTime(ZonedDateTime.now());
                // 设置状态
                report.setReportStatus(ReportStatus.NEW);
                // 保存报告
                reportRepository.save(report);
            } else {
                // 当前的设计方案是一个用户只有一个提报
                report = reportList.get(0);
            }

            final Report reportFinal = report;
            if (ReportStatus.NEW == reportFinal.getReportStatus()
                || ReportStatus.RESET == reportFinal.getReportStatus()) {
                // 提报的自评项目入库
                List<CheckItem> checkItems = checkItemService.findAllByUser(login);
                checkItems.forEach(checkItem -> {
                    ReportItems reportItems = new ReportItems();
                    reportItems.setReport(reportFinal);
                    reportItems.setCheckItem(checkItem);
                    // 保存提报信息
                    reportItemsRepository.save(reportItems);
                });
                // 自查项目更新完成需要更新报告状态为HALT
                if (checkItems.size() > 0) {
                    report.setReportStatus(ReportStatus.HALT);
                    reportRepository.save(report);
                }
            }

        }
        return report;
    }

    /**
     * 更新报告详情
     *
     * @param reportId
     * @param reportItemsList
     * @return
     */
    public Report updateReportItems(Long reportId, String level, List<ReportItems> reportItemsList) {
        // 获取报告
        Report report = reportRepository.findOne(reportId);
        report.setLevel(level);
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
                LocalDate rectificationTime = objects[3] == null ? null : ((Timestamp) objects[3]).toLocalDateTime().toLocalDate();
                String measure = objects[4] == null ? "" : (String) objects[4];
                String result = objects[5] == null ? "" : (String) objects[5];
                Long reportItemId = ((BigInteger) objects[6]).longValue();
                Boolean isAnswer = StringUtils.isNotEmpty((String) objects[7]);
                return new ReportOverviewDTO(reportCreatedTime, reportUsername, reportId, checkItemContent,
                    checkItemCreatedTime, rectificationTime, measure, result, reportItemId, isAnswer);
            })
            .collect(Collectors.toList());
        return reportOverviewDTOList;
    }

    /**
     * 获取用户的主要信息只查询大项
     *
     * @param login
     * @return
     */
    public List<ReportOverviewDTO> getUserReportOverview4MainCheckItem(String login) {
        Report report = getUserReport(login);
        List<Object[]> objectList = reportRepository.findAllReportOverview4MainCheckItem(report.getId());
        List<ReportOverviewDTO> reportOverviewDTOList = objectList
            .stream()
            .map(objects -> {
                LocalDate reportCreatedTime = report.getCreatedTime().toLocalDate();
                String reportUsername = report.getUser().getFirstName();
                Long reportId = objects[0] == null ? report.getId() : ((BigInteger) objects[0]).longValue();
                String checkItemContent = objects[1] == null ? "" : (String) objects[1];
                LocalDate checkItemCreatedTime = objects[2] == null ? null : ((Timestamp) objects[2]).toLocalDateTime().toLocalDate();
                LocalDate rectificationTime = objects[3] == null ? null : ((Timestamp) objects[3]).toLocalDateTime().toLocalDate();
                String measure = objects[4] == null ? "" : (String) objects[4];
                String result = objects[5] == null ? "" : (String) objects[5];
                Long reportItemId = ((BigInteger) objects[6]).longValue();
                Boolean isAnswer = StringUtils.isNotEmpty((String) objects[7]);
                Long checkMainItemId = ((BigInteger) objects[8]).longValue();
                return new ReportOverviewDTO(reportCreatedTime, reportUsername, reportId, checkItemContent,
                    checkItemCreatedTime, rectificationTime, measure, result, reportItemId, isAnswer, checkMainItemId);
            }).collect(Collectors.toList());
        // 过滤掉重复的大项目
        Set<ReportOverviewDTO> reportOverviewDTOSet = reportOverviewDTOList
            .stream()
            .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(ReportOverviewDTO::getCheckMainItemId))));

        return reportOverviewDTOSet.stream().collect(Collectors.toList());
    }

    /**
     * 检查部门报告
     *
     * @param reportId
     * @param reportItemsList
     */
    public void checkReport(Long reportId, List<ReportItems> reportItemsList) {
        // 根据报告获取用户信息
        Report report = this.findOne(reportId);
        if (report == null) {
            throw new BadRequestAlertException("报告[" + reportId + "]不存在", this.getClass().getName(), "报告不存在");
        }
        User reportUser = report.getUser();
        Set<String> authorities = reportUser.getAuthorities().stream().map(auth -> auth.getName()).collect(Collectors.toSet());
        // 管理员需要检查提报信息
        if (authorities.contains(AuthoritiesConstants.ADMIN)
            || authorities.contains(AuthoritiesConstants.BU_ADMIN)
            || authorities.contains(AuthoritiesConstants.TING_ADMIN)
            || authorities.contains(AuthoritiesConstants.JU_ADMIN)
            || authorities.contains(AuthoritiesConstants.CHU_ADMIN)) {
            // 获取部门
            Department department = departmentRepository.findOne(reportUser.getDepartment().getId());
            // 获取子部门,指获取下一级部门所以code后面添加"__"
            String code = department.getCode().substring(0, department.getCode().length() - 2);
            List<Department> childDepartmentList = departmentRepository.findChildDepartmentByDepartmentCode(DeleteFlag.NORMAL.name(), code + "__");
            final Map<Long, ReportItems> reportItemsCMap = new HashMap<>(16);
            // 遍历每个子部门提交的报告信息
            childDepartmentList.forEach(dept -> {
                // 获取该部门的报告
                List<ReportItems> reportItems = reportItemsRepository.findAllByDepartmentId(dept.getId());
                // 遍历报告详情检查是否有"C"
                reportItems.forEach(x -> {
                    if ("C".equals(x.getLevel())) {
                        Long key = x.getCheckItem().getParentId();

                        reportItemsCMap.put(key, x);
                    }
                });
            });
            // 检查用户提交的评分信息
            reportItemsList.forEach(item -> {
                Long key = item.getCheckItem().getId();
                // 子项目有C的项目
                ReportItems reportItems = reportItemsCMap.get(key);
                if (reportItems != null && "A".equals(item.getLevel())) {
                    throw new BadRequestAlertException("请注意考评项目[" + reportItemsCMap.get(key).getCheckItem().getContent() + "]的下级部门有评分C，您选择了评分" + item.getLevel(), this.getClass().getName(), "下级有C考评项目");
                }
            });

        }
    }

    /**
     * 根据登录账号、自评选ID和评分检查
     *
     * @param login
     * @param checkItemId
     * @param level
     */
    public void checkReport(String login, Long checkItemId, String level) {
        // 根据报告获取用户信息
        User reportUser = userService.findOne(login);
        Set<String> authorities = reportUser.getAuthorities().stream().map(auth -> auth.getName()).collect(Collectors.toSet());
        // 管理员需要检查提报信息
        if (authorities.contains(AuthoritiesConstants.ADMIN)
            || authorities.contains(AuthoritiesConstants.BU_ADMIN)
            || authorities.contains(AuthoritiesConstants.TING_ADMIN)
            || authorities.contains(AuthoritiesConstants.JU_ADMIN)
            || authorities.contains(AuthoritiesConstants.CHU_ADMIN)) {
            // 获取部门
            Department department = departmentRepository.findOne(reportUser.getDepartment().getId());
            // 获取子部门,指获取下一级部门所以code后面添加"__"
            String code = department.getCode().substring(0, department.getCode().length() - 2);
            List<Department> childDepartmentList = departmentRepository.findChildDepartmentByDepartmentCode(DeleteFlag.NORMAL.name(), code + "__");
            final Map<Long, String> reportItemsCMap = new HashMap<>(16);
            // 遍历每个子部门提交的报告信息
            childDepartmentList.forEach(dept -> {
                // 获取该部门的报告
                List<ReportItems> reportItems = reportItemsRepository.findAllByDepartmentId(dept.getId());
                // 遍历报告详情检查是否有"C"
                reportItems.forEach(x -> {
                    if ("C".equals(x.getLevel())) {
                        Long key = x.getCheckItem().getParentId();
                        String value = x.getCheckItem().getId().toString();
                        reportItemsCMap.put(key, value);
                    }
                });
            });
            // 检查用户提交的评分信息
            Long key = checkItemId;
            // 子项目有C的项目
            String subCheckItemCId = reportItemsCMap.get(key);
            if (subCheckItemCId != null && "A".equals(level)) {
                CheckItem checkItem = checkItemRepository.findOne(key);
                throw new BadRequestAlertException("请注意您的下级部门在考评项目[" + checkItem.getContent() + "]有评分C，您选择了评分" + level, this.getClass().getName(), "下级有C考评项目");
            }
        }
    }

    /**
     * 获取部门详情
     *
     * @param reportId
     * @return
     */
    public ReportDetailVM getReportDetail(Long reportId) {
        Report report = reportRepository.findOne(reportId);
        if (report == null)
            throw new BadRequestAlertException("报告" + reportId + "不存在", this.getClass().getName(), "报告不存在");
        List<ReportItems> reportItemsList = reportItemsRepository.findAllByReport(report);
        return new ReportDetailVM(report, reportItemsList);
    }
}
