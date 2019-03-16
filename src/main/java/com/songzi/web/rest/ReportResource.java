package com.songzi.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.songzi.domain.Report;
import com.songzi.domain.ReportItems;
import com.songzi.service.ReportItemsService;
import com.songzi.service.ReportService;
import com.songzi.web.rest.errors.BadRequestAlertException;
import com.songzi.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sun.rmi.runtime.Log;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Report.
 */
@RestController
@RequestMapping("/api")
public class ReportResource {

    private final Logger log = LoggerFactory.getLogger(ReportResource.class);

    @Autowired
    private ReportItemsService reportItemsService;

    private static final String ENTITY_NAME = "report";

    private final ReportService reportService;

    public ReportResource(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * POST  /reports : Create a new report.
     *
     * @param report the report to create
     * @return the ResponseEntity with status 201 (Created) and with body the new report, or with status 400 (Bad Request) if the report has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/reports")
    @Timed
    public ResponseEntity<Report> createReport(@Valid @RequestBody Report report) throws URISyntaxException {
        log.debug("REST request to save Report : {}", report);
        if (report.getId() != null) {
            throw new BadRequestAlertException("A new report cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Report result = reportService.save(report);
        return ResponseEntity.created(new URI("/api/reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /reports : Updates an existing report.
     *
     * @param report the report to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated report,
     * or with status 400 (Bad Request) if the report is not valid,
     * or with status 500 (Internal Server Error) if the report couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/reports")
    @Timed
    public ResponseEntity<Report> updateReport(@Valid @RequestBody Report report) throws URISyntaxException {
        log.debug("REST request to update Report : {}", report);
        if (report.getId() == null) {
            return createReport(report);
        }
        Report result = reportService.save(report);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, report.getId().toString()))
            .body(result);
    }

    /**
     * GET  /reports : get all the reports.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of reports in body
     */
    @GetMapping("/reports")
    @Timed
    public List<Report> getAllReports() {
        log.debug("REST request to get all Reports");
        return reportService.findAll();
    }

    /**
     * GET  /reports/:id : get the "id" report.
     *
     * @param id the id of the report to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the report, or with status 404 (Not Found)
     */
    @GetMapping("/reports/{id}")
    @Timed
    public ResponseEntity<Report> getReport(@PathVariable Long id) {
        log.debug("REST request to get Report : {}", id);
        Report report = reportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(report));
    }

    /**
     * DELETE  /reports/:id : delete the "id" report.
     *
     * @param id the id of the report to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/reports/{id}")
    @Timed
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        log.debug("REST request to delete Report : {}", id);
        reportService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * 获取用户提报信息
     *
     * @param login
     * @return
     */
    @GetMapping("/reports/users")
    @Timed
    @ApiOperation("获取用户的提报信息")
    public ResponseEntity<Report> gerUserReport(@RequestParam String login) {
        log.debug("获取用户的提报信息：{}", login);
        Report report = reportService.getUserReport(login);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(report));
    }

    /**
     * 获取提报的详情信息
     *
     * @param reportId
     * @return
     */
    @GetMapping("/reports/report-items")
    @ApiOperation("获取提报的详情信息")
    public List<ReportItems> getReportItemsByReport(@RequestParam Long reportId) {
        log.debug("获取报告{}详情", reportId);
        return reportItemsService.findAllByReport(reportService.findOne(reportId));
    }

    /**
     * 提交报告
     * @param reportId
     * @param reportItemsList
     * @return
     */
    @PutMapping("/reports/submit")
    @Timed
    @ApiOperation("提交报告")
    public ResponseEntity<Report> updateReportDetail(@RequestParam Long reportId, @RequestBody List<ReportItems> reportItemsList) {
        log.debug("更新报告{}的详情", reportId);
        Report report = reportService.updateReportItems(reportId, reportItemsList);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(report));
    }
}
