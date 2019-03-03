package com.songzi.web.rest.vm;

import liquibase.util.StringUtils;

import java.time.LocalDate;

/**
 * Created by Ke Qingyuan on 2019/3/3.
 */
public class YiliuProblem {

    /**
     * 问题类型：1：泄密事件、2：保密隐患、3：管理不力
     */
    private String problemType;

    private LocalDate detectedDate;

    private String description;

    private YiliuProblem(){}
    public YiliuProblem(String jsonStr) {
        super();
        if (StringUtils.isNotEmpty(jsonStr)){

        }
    }
    public String getProblemType() {
        return problemType;
    }

    public void setProblemType(String problemType) {
        this.problemType = problemType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDetectedDate() {
        return detectedDate;
    }

    public void setDetectedDate(LocalDate detectedDate) {
        this.detectedDate = detectedDate;
    }
}
