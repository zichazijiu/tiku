package com.songzi.service.dto;


import java.time.LocalDate;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Office entity.
 */
public class OfficeDTO implements Serializable {

    private Long id;

    private String name;

    private String office_status;

    private String del_flag;

    private String office_type;

    private LocalDate created_date;

    private String created_by;

    private LocalDate last_modified_date;

    private String last_modified_by;

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

    public String getOffice_status() {
        return office_status;
    }

    public void setOffice_status(String office_status) {
        this.office_status = office_status;
    }

    public String getDel_flag() {
        return del_flag;
    }

    public void setDel_flag(String del_flag) {
        this.del_flag = del_flag;
    }

    public String getOffice_type() {
        return office_type;
    }

    public void setOffice_type(String office_type) {
        this.office_type = office_type;
    }

    public LocalDate getCreated_date() {
        return created_date;
    }

    public void setCreated_date(LocalDate created_date) {
        this.created_date = created_date;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public LocalDate getLast_modified_date() {
        return last_modified_date;
    }

    public void setLast_modified_date(LocalDate last_modified_date) {
        this.last_modified_date = last_modified_date;
    }

    public String getLast_modified_by() {
        return last_modified_by;
    }

    public void setLast_modified_by(String last_modified_by) {
        this.last_modified_by = last_modified_by;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OfficeDTO officeDTO = (OfficeDTO) o;
        if(officeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), officeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OfficeDTO{" +
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
