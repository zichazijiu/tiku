package com.songzi.service;

import com.songzi.domain.*;
import com.songzi.domain.enumeration.DeleteFlag;
import com.songzi.domain.enumeration.ReportStatus;
import com.songzi.repository.*;
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

import javax.security.auth.login.AccountExpiredException;
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

    @Autowired
    private ReleaseHistoryRepository releaseHistoryRepository;

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
    public List<Report> getUserReport(String login) {
        List<Report> reports = null;
        if (StringUtils.isNotEmpty(login)) {
            User user = userService.getUserWithAuthoritiesByLogin(login).get();
            // 用户部门
            Department department = user.getDepartment();
            // 当前用户部门code
            String code = department.getCode();
            // 获取上级部门
            String[] parentCode = department.getParentCodes().split(",");
            // 检查是机关单位还是地方机关,如果是机关单位获取上级部门的发布历史
            if (code.substring(code.length() - 4).startsWith("01")) {
                if (parentCode.length > 1) {
                    code = parentCode[parentCode.length - 2];
                } else {
                    code = parentCode[0];
                }
            } else {
                // 地方机关，获取同级的机关单位的发布历史
                StringBuilder sb = new StringBuilder(code);
                sb.replace(code.length() - 3, code.length(), "___");
                code = sb.toString();
            }

            // 检查该部门有没有新的发布历史
            List<ReleaseHistory> releaseHistoryList = releaseHistoryRepository.findAllByDepartmentCode(code);
            if (releaseHistoryList == null) {
                // 没有发布历史
                return reports;
            } else {
                // 有发布历史，检查是否有新的发布历史
            }
            // 有新的发布历史
            reports = reportRepository.findByUserId(user.getId());
            if (reports == null || reports.size() < 1) {
                Report report = new Report();
                // 设置用户
                report.setUser(user);
                // 设置时间
                report.setCreatedTime(ZonedDateTime.now());
                // 设置状态
                report.setReportStatus(ReportStatus.NEW);
                // 保存报告
                Report report1 = reportRepository.save(report);
                // 提报的自评项目入库
                List<CheckItem> checkItems = checkItemService.findAllByUser(login);
                checkItems.forEach(checkItem -> {
                    ReportItems reportItems = new ReportItems();
                    reportItems.setReport(report1);
                    reportItems.setCheckItem(checkItem);
                    // 保存提报信息
                    reportItemsRepository.save(reportItems);
                });

                reports.add(report1);
            }

        }
        return reports;
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
        List<Report> reports = getUserReport(login);
        List<ReportOverviewDTO> reportOverviewDTOList = null;
        for (Report report : reports) {

            List<Object[]> objectList = reportRepository.findAllReportOverview(report.getId());
            reportOverviewDTOList = objectList
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
        }
        return reportOverviewDTOList;
    }

    /**
     * 获取用户的主要信息只查询大项
     *
     * @param login
     * @return
     */
    public List<ReportOverviewDTO> getUserReportOverview4MainCheckItem(String login) {
        // 获取用户的报告
        List<Report> reports = getUserReport(login);
        // map过滤器
        Map<Long, ReportOverviewDTO> mapFilter = new HashMap<>();
        for (Report report : reports) {
            // 查找报告关联的自查项目信息
            List<Object[]> objectList = reportRepository.findAllReportOverview4MainCheckItem(report.getId());
            List<ReportOverviewDTO> reportOverviewDTOList = new ArrayList<>();
            objectList.forEach
                (objects -> {
                    String checkItemContent = objects[1] == null ? "" : (String) objects[1];
                    if (StringUtils.isNotEmpty(checkItemContent)) {

                        LocalDate reportCreatedTime = report.getCreatedTime().toLocalDate();
                        String reportUsername = report.getUser().getFirstName();
                        Long reportId = objects[0] == null ? report.getId() : ((BigInteger) objects[0]).longValue();
                        LocalDate checkItemCreatedTime = objects[2] == null ? null : ((Timestamp) objects[2]).toLocalDateTime().toLocalDate();
                        LocalDate rectificationTime = objects[3] == null ? null : ((Timestamp) objects[3]).toLocalDateTime().toLocalDate();
                        String measure = objects[4] == null ? "" : (String) objects[4];
                        String result = objects[5] == null ? "" : (String) objects[5];
                        Long reportItemId = ((BigInteger) objects[6]).longValue();
                        Boolean isAnswer = StringUtils.isNotEmpty((String) objects[7]);
                        Long checkMainItemId = ((BigInteger) objects[8]).longValue();

                        // 1. 检查是否有已经保存的对象
                        ReportOverviewDTO old = mapFilter.get(checkMainItemId);
                        // 没有对象，保存新对象
                        if (old == null) {
                            ReportOverviewDTO item = new ReportOverviewDTO(reportCreatedTime, reportUsername, reportId, checkItemContent,
                                checkItemCreatedTime, rectificationTime, measure, result, reportItemId, isAnswer, checkMainItemId);

                            mapFilter.put(item.getCheckMainItemId(), item);
                        } else {
                            // 有旧对象，则要和新对象对比：保留有整改时间且整改是最新的对象
                            if (rectificationTime != null) {
                                // 新的有整改时间，对比新旧对象的整改时间的大小
                                if (old.getRectificationTime() == null || old.getRectificationTime().isBefore(rectificationTime)) {
                                    ReportOverviewDTO item = new ReportOverviewDTO(reportCreatedTime, reportUsername, reportId, checkItemContent,
                                        checkItemCreatedTime, rectificationTime, measure, result, reportItemId, isAnswer, checkMainItemId);

                                    mapFilter.put(item.getCheckMainItemId(), item);
                                }
                            }
                        }

                    }
                });
        }
        return mapFilter.values().stream().collect(Collectors.toList());
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
     * 获取报告详情
     *
     * @param reportId
     * @param checkMainItemId
     * @return
     */
    public ReportDetailVM getReportDetail(Long reportId, Long checkMainItemId) {
        Report report = reportRepository.findOne(reportId);
        if (report == null) {
            throw new BadRequestAlertException("报告" + reportId + "不存在", this.getClass().getName(), "报告不存在");
        }
        List<ReportItems> reportItemsList;
        if (checkMainItemId != null) {
            reportItemsList = reportItemsRepository.findAllByReportAndCheckItemParentId(report, checkMainItemId);
        } else {
            reportItemsList = reportItemsRepository.findAllByReport(report);
        }
        return new ReportDetailVM(report, reportItemsList);
    }
}
