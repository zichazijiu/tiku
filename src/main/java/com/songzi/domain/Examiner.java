package com.songzi.domain;


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

    @Column


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
