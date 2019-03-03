package com.songzi.service.dto;

import com.songzi.domain.Department;
import com.songzi.domain.Menu;

import java.util.List;

/**
 * Created by Ke Qingyuan on 2019/3/3.
 */
public class HomepageDTO {

    private List<Menu> menuList;

    private List<Department> departmentList;

    public List<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
    }

    public List<Department> getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(List<Department> departmentList) {
        this.departmentList = departmentList;
    }
}
