package com.songzi.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.Instant;


@ApiModel(description = "项目")
public class ProjectDTO {

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "做题时间")
    private Instant date;

    @ApiModelProperty(value = "分数")
    private Long examineId;

    @ApiModelProperty(value = "分数")
    private Integer score;

    public ProjectDTO(String name, String description, Instant date, Long examineId, Integer score) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.examineId = examineId;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Long getExamineId() {
        return examineId;
    }

    public void setExamineId(Long examineId) {
        this.examineId = examineId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

}
