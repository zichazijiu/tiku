package com.songzi.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A CheckItemAnswer.
 */
@Entity
@Table(name = "check_item_answer")
public class CheckItemAnswer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "yiliu_problems")
    private String yiliuProblems;

    @Column(name = "zhenggai_info")
    private String zhenggaiInfo;

    @Column(name = "result")
    private String result;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private LocalDate createdDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItemId() {
        return itemId;
    }

    public CheckItemAnswer itemId(Long itemId) {
        this.itemId = itemId;
        return this;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getYiliuProblems() {
        return yiliuProblems;
    }

    public CheckItemAnswer yiliuProblems(String yiliuProblems) {
        this.yiliuProblems = yiliuProblems;
        return this;
    }

    public void setYiliuProblems(String yiliuProblems) {
        this.yiliuProblems = yiliuProblems;
    }

    public String getZhenggaiInfo() {
        return zhenggaiInfo;
    }

    public CheckItemAnswer zhenggaiInfo(String zhenggaiInfo) {
        this.zhenggaiInfo = zhenggaiInfo;
        return this;
    }

    public void setZhenggaiInfo(String zhenggaiInfo) {
        this.zhenggaiInfo = zhenggaiInfo;
    }

    public String getResult() {
        return result;
    }

    public CheckItemAnswer result(String result) {
        this.result = result;
        return this;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public CheckItemAnswer createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public CheckItemAnswer createdDate(LocalDate createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
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
        CheckItemAnswer checkItemAnswer = (CheckItemAnswer) o;
        if (checkItemAnswer.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), checkItemAnswer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CheckItemAnswer{" +
            "id=" + getId() +
            ", itemId=" + getItemId() +
            ", yiliuProblems='" + getYiliuProblems() + "'" +
            ", zhenggaiInfo='" + getZhenggaiInfo() + "'" +
            ", result='" + getResult() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }

}
