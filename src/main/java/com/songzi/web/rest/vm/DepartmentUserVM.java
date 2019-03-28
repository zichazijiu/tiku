package com.songzi.web.rest.vm;

/**
 * Created by Ke Qingyuan on 2019/3/28.
 */
public class DepartmentUserVM {
    private Long departmentId;
    private Long[] userIds;

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long[] getUserIds() {
        return userIds;
    }

    public void setUserIds(Long[] userIds) {
        this.userIds = userIds;
    }
}
