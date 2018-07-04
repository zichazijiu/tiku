package com.songzi.service.dto;

import com.songzi.domain.enumeration.Status;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;


@ApiModel(description = "项目")
public class ProjectDTO {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "做题时间")
    private Instant date;

    @ApiModelProperty(value = "项目状态")
    private Status status;

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

    @ApiModelProperty(value = "规定时间")
    private Integer duration;

    private List<SubjectDTO> subjectDTOList;

    public ProjectDTO(){

    }

    public ProjectDTO(Long id,String name, String description, Instant date, Long examineId, Integer score,Instant createdDate,String createdBy,Integer duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
        this.examineId = examineId;
        this.score = score;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.duration = duration;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<SubjectDTO> getSubjectDTOList() {
        return subjectDTOList;
    }

    public void setSubjectDTOList(List<SubjectDTO> subjectDTOList) {
        this.subjectDTOList = subjectDTOList;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}
