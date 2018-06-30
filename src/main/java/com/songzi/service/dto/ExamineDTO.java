package com.songzi.service.dto;

import java.util.List;

public class ExamineDTO {

    private Long id;

    private String name;

    private String status;

    private Long departmentId;

    private Integer duration;

    private Integer score;

    private Long userId;

    private String result;

    private ProjectDTO project;

    private List<ExamineSubjectDTO> examineSubjectDTOList;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public ProjectDTO getProject() {
        return project;
    }

    public void setProject(ProjectDTO project) {
        this.project = project;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<ExamineSubjectDTO> getExamineSubjectDTOList() {
        return examineSubjectDTOList;
    }

    public void setExamineSubjectDTOList(List<ExamineSubjectDTO> examineSubjectDTOList) {
        this.examineSubjectDTOList = examineSubjectDTOList;
    }
}
