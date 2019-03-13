package com.songzi.service.dto;

import java.util.Date;
import java.util.List;

/**
 * Created by Ke Qingyuan on 2019/3/13.
 */
public class CheckItemOverviewDTO {

    private String deptName;

    private String checkResult;

    private String createdUser;

    private String createdDate;

    private List<String> zhenggaiList;

    private Date checkDate;

    private List<String> checkDescription;

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(String checkResult) {
        this.checkResult = checkResult;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public List<String> getZhenggaiList() {
        return zhenggaiList;
    }

    public void setZhenggaiList(List<String> zhenggaiList) {
        this.zhenggaiList = zhenggaiList;
    }

    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    public List<String> getCheckDescription() {
        return checkDescription;
    }

    public void setCheckDescription(List<String> checkDescription) {
        this.checkDescription = checkDescription;
    }
}
