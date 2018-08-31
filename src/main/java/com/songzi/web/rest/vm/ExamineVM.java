package com.songzi.web.rest.vm;

import com.songzi.domain.enumeration.ExamineType;

public class ExamineVM {

    private Long id;

    private Long projectId;

    private ExamineType examineType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public ExamineType getExamineType() {
        return examineType;
    }

    public void setExamineType(ExamineType examineType) {
        this.examineType = examineType;
    }
}
