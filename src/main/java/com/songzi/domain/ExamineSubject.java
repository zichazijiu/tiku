package com.songzi.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A ExamineSubject.
 */
@Entity
@Table(name = "examine_subject")
public class ExamineSubject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_year")
    private String year;

    @Column(name = "month")
    private String month;

    @Column(name = "subject_id")
    private Long subjectId;

    @Column(name = "right_time")
    private Integer rightTime;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public ExamineSubject year(String year) {
        this.year = year;
        return this;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public ExamineSubject month(String month) {
        this.month = month;
        return this;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public ExamineSubject subjectId(Long subjectId) {
        this.subjectId = subjectId;
        return this;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public Integer getRightTime() {
        return rightTime;
    }

    public ExamineSubject rightTime(Integer rightTime) {
        this.rightTime = rightTime;
        return this;
    }

    public void setRightTime(Integer rightTime) {
        this.rightTime = rightTime;
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
        ExamineSubject examineSubject = (ExamineSubject) o;
        if (examineSubject.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), examineSubject.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ExamineSubject{" +
            "id=" + getId() +
            ", year='" + getYear() + "'" +
            ", month='" + getMonth() + "'" +
            ", subjectId=" + getSubjectId() +
            ", rightTime=" + getRightTime() +
            "}";
    }
}
