package com.songzi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Office.
 */
@Entity
@Table(name = "office")
public class Office implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "office_status")
    private String office_status;

    @Column(name = "del_flag")
    private String del_flag;

    @Column(name = "office_type")
    private String office_type;

    @Column(name = "created_date")
    private LocalDate created_date;

    @Column(name = "created_by")
    private String created_by;

    @Column(name = "last_modified_date")
    private LocalDate last_modified_date;

    @Column(name = "last_modified_by")
    private String last_modified_by;

    @ManyToMany(mappedBy = "offices")
    @JsonIgnore
    private Set<Department> departments = new HashSet<>();

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

    public Office name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOffice_status() {
        return office_status;
    }

    public Office office_status(String office_status) {
        this.office_status = office_status;
        return this;
    }

    public void setOffice_status(String office_status) {
        this.office_status = office_status;
    }

    public String getDel_flag() {
        return del_flag;
    }

    public Office del_flag(String del_flag) {
        this.del_flag = del_flag;
        return this;
    }

    public void setDel_flag(String del_flag) {
        this.del_flag = del_flag;
    }

    public String getOffice_type() {
        return office_type;
    }

    public Office office_type(String office_type) {
        this.office_type = office_type;
        return this;
    }

    public void setOffice_type(String office_type) {
        this.office_type = office_type;
    }

    public LocalDate getCreated_date() {
        return created_date;
    }

    public Office created_date(LocalDate created_date) {
        this.created_date = created_date;
        return this;
    }

    public void setCreated_date(LocalDate created_date) {
        this.created_date = created_date;
    }

    public String getCreated_by() {
        return created_by;
    }

    public Office created_by(String created_by) {
        this.created_by = created_by;
        return this;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public LocalDate getLast_modified_date() {
        return last_modified_date;
    }

    public Office last_modified_date(LocalDate last_modified_date) {
        this.last_modified_date = last_modified_date;
        return this;
    }

    public void setLast_modified_date(LocalDate last_modified_date) {
        this.last_modified_date = last_modified_date;
    }

    public String getLast_modified_by() {
        return last_modified_by;
    }

    public Office last_modified_by(String last_modified_by) {
        this.last_modified_by = last_modified_by;
        return this;
    }

    public void setLast_modified_by(String last_modified_by) {
        this.last_modified_by = last_modified_by;
    }

    public Set<Department> getDepartments() {
        return departments;
    }

    public Office departments(Set<Department> departments) {
        this.departments = departments;
        return this;
    }

    public Office addDepartment(Department department) {
        this.departments.add(department);
        department.getOffices().add(this);
        return this;
    }

    public Office removeDepartment(Department department) {
        this.departments.remove(department);
        department.getOffices().remove(this);
        return this;
    }

    public void setDepartments(Set<Department> departments) {
        this.departments = departments;
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
        Office office = (Office) o;
        if (office.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), office.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Office{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", office_status='" + getOffice_status() + "'" +
            ", del_flag='" + getDel_flag() + "'" +
            ", office_type='" + getOffice_type() + "'" +
            ", created_date='" + getCreated_date() + "'" +
            ", created_by='" + getCreated_by() + "'" +
            ", last_modified_date='" + getLast_modified_date() + "'" +
            ", last_modified_by='" + getLast_modified_by() + "'" +
            "}";
    }
}
