package com.songzi.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.Instant;
import java.time.LocalDate;


@ApiModel(description = "项目")
public class ProjectDTO {

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "做题时间")
    private Instant date;

    @ApiModelProperty(value = "考试id")
    private Long examineId;

    @ApiModelProperty(value = "分数")
    private Integer score;

    @ApiModelProperty(value = "用户是否答过题标志")
    private boolean hasExamineFlag;

    @ApiModelProperty(value = "创建时间")
    private Instant createdDate;

    @ApiModelProperty(value = "创建人")
    private String createdBy;

    public ProjectDTO(){

    }

    public ProjectDTO(String name, String description, Instant date, Long examineId, Integer score,Instant createDate,String createBy) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.examineId = examineId;
        this.score = score;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
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

    public boolean isHasExamineFlag() {
        if(this.examineId == null){
            this.hasExamineFlag = false;
        }else{
            this.hasExamineFlag = true;
        }
        return hasExamineFlag;
    }

    public void setHasExamineFlag(boolean hasExamineFlag) {
        this.hasExamineFlag = hasExamineFlag;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
