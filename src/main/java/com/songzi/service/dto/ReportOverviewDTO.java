package com.songzi.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDate;

/**
 * Created by Ke Qingyuan on 2019/3/16.
 */
@ApiModel("提报")
public class ReportOverviewDTO {

    @ApiModelProperty(value = "报告id")
    private Long reportId;

    @ApiModelProperty("提报时间")
    private LocalDate reportCreatedTime;

    @ApiModelProperty("考评项")
    private String checkItemContent;

    @ApiModelProperty("考评项创建时间")
    private LocalDate checkItemCreatedTime;

    @ApiModelProperty("整改时间")
    private LocalDate rectificationTime;

    @ApiModelProperty("提报人")
    private String reportUsername;

    @ApiModelProperty("整改措施")
    private String measure;

    @ApiModelProperty("整改结果")
    private String result;

    public ReportOverviewDTO(LocalDate reportCreatedTime, String reportUsername, Long reportId, String checkItemContent, LocalDate checkItemCreatedTime, LocalDate rectificationTime, String measure, String result) {
        this.reportCreatedTime = reportCreatedTime;
        this.reportUsername = reportUsername;
        this.reportId = reportId;
        this.checkItemContent = checkItemContent;
        this.checkItemCreatedTime = checkItemCreatedTime;
        this.rectificationTime = rectificationTime;
        this.measure = measure;
        this.result =result;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public LocalDate getReportCreatedTime() {
        return reportCreatedTime;
    }

    public void setReportCreatedTime(LocalDate reportCreatedTime) {
        this.reportCreatedTime = reportCreatedTime;
    }

    public LocalDate getRectificationTime() {
        return rectificationTime;
    }

    public void setRectificationTime(LocalDate rectificationTime) {
        this.rectificationTime = rectificationTime;
    }

    public String getReportUsername() {
        return reportUsername;
    }

    public void setReportUsername(String reportUsername) {
        this.reportUsername = reportUsername;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getCheckItemContent() {
        return checkItemContent;
    }

    public void setCheckItemContent(String checkItemContent) {
        this.checkItemContent = checkItemContent;
    }

    public LocalDate getCheckItemCreatedTime() {
        return checkItemCreatedTime;
    }

    public void setCheckItemCreatedTime(LocalDate checkItemCreatedTime) {
        this.checkItemCreatedTime = checkItemCreatedTime;
    }
}
