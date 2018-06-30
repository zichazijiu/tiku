package com.songzi.domain;


import com.songzi.domain.enumeration.Sex;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Examiner.
 */
@Entity
@Table(name = "examiner")
public class Examiner implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "department_id", nullable = false)
    private Long departmentId;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull
    @ApiModelProperty(name = "自查次数")
    @Column(name = "examiner_time", nullable = false)
    private Integer time;

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

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Examiner name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public Examiner departmentId(Long departmentId) {
        this.departmentId = departmentId;
        return this;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getUserId() {
        return userId;
    }

    public Examiner userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getTime() {
        return time;
    }

    public Examiner time(Integer time) {
        this.time = time;
        return this;
    }

    public void setTime(Integer time) {
        this.time = time;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove


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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Examiner examiner = (Examiner) o;
        if (examiner.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), examiner.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Examiner{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", departmentId=" + getDepartmentId() +
            ", userId=" + getUserId() +
            ", time=" + getTime() +
            "}";
    }
}
