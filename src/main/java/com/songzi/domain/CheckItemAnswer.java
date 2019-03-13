package com.songzi.domain;


import org.hibernate.criterion.Example;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A CheckItemAnswer.
 */
@Entity
@Table(name = "check_item_answer")
public class CheckItemAnswer extends Example implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "result")
    private String result;

    @Column(name = "created_by")
    private String createdBy;

    @NotNull
    @Column(name = "dept_id", nullable = false)
    private Long deptId;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private ZonedDateTime createdDate;

    /**
     * Allow subclasses to instantiate as needed.
     *
     * @param exampleEntity The example bean
     * @param selector      The property selector to use
     */
    protected CheckItemAnswer(Object exampleEntity, PropertySelector selector) {
        super(exampleEntity, selector);
    }

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

    public Long getDeptId() {
        return deptId;
    }

    public CheckItemAnswer deptId(Long deptId) {
        this.deptId = deptId;
        return this;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public CheckItemAnswer createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
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
            ", result='" + getResult() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", deptId=" + getDeptId() +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
