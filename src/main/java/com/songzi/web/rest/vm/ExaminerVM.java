package com.songzi.web.rest.vm;

import com.songzi.domain.enumeration.AuthoritiesType;
import com.songzi.domain.enumeration.Sex;

import java.util.List;

public class ExaminerVM {

    private Long id;

    private String name;

    private Long departmentId;

    private String cellPhone;

    private String email;

    private Sex sex;

    private String birth;

    private String location;

    private String phone;

    private String address;

    private List<AuthoritiesType> authoritiesType;

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

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<AuthoritiesType> getAuthoritiesType() {
        return authoritiesType;
    }

    public void setAuthoritiesType(List<AuthoritiesType> authoritiesType) {
        this.authoritiesType = authoritiesType;
    }
}
