package com.songzi.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.Instant;
import java.time.LocalDate;

/**
 * Created by Ke Qingyuan on 2019/3/16.
 */
@ApiModel("提报")
public class ReportDTO {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty("提报时间")
    private LocalDate createdTime;
}
