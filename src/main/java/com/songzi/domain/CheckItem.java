package com.songzi.domain;


import com.songzi.domain.enumeration.CheckItemType;
import com.songzi.domain.enumeration.DeleteFlag;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A CheckItem.
 */
@Entity
@Table(name = "check_item")
public class CheckItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "description")
    private String description;

    @Column(name = "parent_id")
    private Long parentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_type")
    private CheckItemType itemType;

    @Enumerated(EnumType.STRING)
    @Column(name = "del_flag")
    private DeleteFlag delFlag;

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

    public String getContent() {
        return content;
    }

    public CheckItem content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public CheckItem description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getParentId() {
        return parentId;
    }

    public CheckItem parentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public CheckItemType getItemType() {
        return itemType;
    }

    public CheckItem itemType(CheckItemType itemType) {
        this.itemType = itemType;
        return this;
    }

    public void setItemType(CheckItemType itemType) {
        this.itemType = itemType;
    }

    public DeleteFlag getDelFlag() {
        return delFlag;
    }

    public CheckItem delFlag(DeleteFlag delFlag) {
        this.delFlag = delFlag;
        return this;
    }

    public void setDelFlag(DeleteFlag delFlag) {
        this.delFlag = delFlag;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public CheckItem createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public CheckItem createdDate(LocalDate createdDate) {
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
        CheckItem checkItem = (CheckItem) o;
        if (checkItem.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), checkItem.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CheckItem{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", description='" + getDescription() + "'" +
            ", parentId=" + getParentId() +
            ", itemType='" + getItemType() + "'" +
            ", delFlag='" + getDelFlag() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }

}
