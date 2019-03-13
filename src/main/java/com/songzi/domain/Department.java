package com.songzi.domain;


import com.songzi.domain.enumeration.DeleteFlag;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Department.
 */
@Entity
@Table(name = "department")
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "parent_id")
    private Long parentId;

    @NotNull
    @Column(name = "department_status", nullable = false)
    private String departmentStatus;

    @NotNull
    @Column(name = "department_type", nullable = false)
    private String departmentType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "del_flag", nullable = false)
    private DeleteFlag delFlag = DeleteFlag.NORMAL;

    @Column(name = "parent_codes")
    private String parentCodes;

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

    public Department name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public Department code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getParentId() {
        return parentId;
    }

    public Department parentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getDepartmentStatus() {
        return departmentStatus;
    }

    public Department departmentStatus(String departmentStatus) {
        this.departmentStatus = departmentStatus;
        return this;
    }

    public void setDepartmentStatus(String departmentStatus) {
        this.departmentStatus = departmentStatus;
    }

    public String getDepartmentType() {
        return departmentType;
    }

    public Department departmentType(String departmentType) {
        this.departmentType = departmentType;
        return this;
    }

    public void setDepartmentType(String departmentType) {
        this.departmentType = departmentType;
    }

    public DeleteFlag getDelFlag() {
        return delFlag;
    }

    public Department delFlag(DeleteFlag delFlag) {
        this.delFlag = delFlag;
        return this;
    }

    public void setDelFlag(DeleteFlag delFlag) {
        this.delFlag = delFlag;
    }

    public String getParentCodes() {
        return parentCodes;
    }

    public Department parentCodes(String parentCodes) {
        this.parentCodes = parentCodes;
        return this;
    }

    public void setParentCodes(String parentCodes) {
        this.parentCodes = parentCodes;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Department department = (Department) o;
        if (department.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), department.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Department{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", parentId=" + getParentId() +
            ", departmentStatus='" + getDepartmentStatus() + "'" +
            ", departmentType='" + getDepartmentType() + "'" +
            ", delFlag='" + getDelFlag() + "'" +
            ", parentCodes='" + getParentCodes() + "'" +
            "}";
    }
}
