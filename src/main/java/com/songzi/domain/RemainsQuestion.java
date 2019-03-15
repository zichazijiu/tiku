package com.songzi.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import com.songzi.domain.enumeration.QuestionType;

/**
 * A RemainsQuestion.
 */
@Entity
@Table(name = "check_item_answer_remains")
public class RemainsQuestion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", nullable = false)
    private QuestionType questionType;

    @NotNull
    @Column(name = "created_time", nullable = false)
    private ZonedDateTime createdTime;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "item_answer_id", nullable = false)
    private Long itemAnswerId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public RemainsQuestion questionType(QuestionType questionType) {
        this.questionType = questionType;
        return this;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public ZonedDateTime getCreatedTime() {
        return createdTime;
    }

    public RemainsQuestion createdTime(ZonedDateTime createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    public void setCreatedTime(ZonedDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public String getDescription() {
        return description;
    }

    public RemainsQuestion description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getItemAnswerId() {
        return itemAnswerId;
    }

    public RemainsQuestion itemAnswerId(Long itemAnswerId) {
        this.itemAnswerId = itemAnswerId;
        return this;
    }

    public void setItemAnswerId(Long itemAnswerId) {
        this.itemAnswerId = itemAnswerId;
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
        RemainsQuestion remainsQuestion = (RemainsQuestion) o;
        if (remainsQuestion.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), remainsQuestion.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RemainsQuestion{" +
            "id=" + getId() +
            ", questionType='" + getQuestionType() + "'" +
            ", createdTime='" + getCreatedTime() + "'" +
            ", description='" + getDescription() + "'" +
            ", itemAnswerId=" + getItemAnswerId() +
            "}";
    }
}
