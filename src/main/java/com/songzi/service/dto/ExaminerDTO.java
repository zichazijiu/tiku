package com.songzi.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.songzi.domain.enumeration.Sex;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.Instant;

@ApiModel(description = "考评员")
public class ExaminerDTO {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @JsonIgnore
    private Long userId;

    @ApiModelProperty(value = "登录名")
    private String login;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "考评次数")
    private Integer time;

    @ApiModelProperty(value = "部门名称")
    private String departmentName;

    @ApiModelProperty(value = "部门Id")
    private Long departmentId;

    @ApiModelProperty(name = "手机号")
    @Column(name = "cell_phone")
    private String cellPhone;

    @ApiModelProperty(name = "邮箱")
    @Column(name = "email")
    private String email;

    @ApiModelProperty(name = "性别")
    @Enumerated(EnumType.STRING)
    @Column(name = "sex")
    private Sex sex;

    @ApiModelProperty(name = "生日")
    @Column(name = "birth")
    private String birth;

    @ApiModelProperty(name = "区域")
    @Column(name = "location")
    private String location;

    @ApiModelProperty(name = "座机")
    @Column(name = "phone")
    private String phone;

    @ApiModelProperty(name = "地址")
    @Column(name = "address")
    private String address;

    private String createdBy;

    private Instant createdDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }
}
