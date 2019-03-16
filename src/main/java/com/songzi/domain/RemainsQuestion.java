package com.songzi.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.songzi.domain.enumeration.QuestionType;

/**
 * A RemainsQuestion.
 */
@Entity
@Table(name = "remains_question")
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

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnore
    private ReportItems reportItems;

    @OneToOne
    @JoinColumn(unique = true)
    private Rectification rectification;

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

    public ReportItems getReportItems() {
        return reportItems;
    }

    public RemainsQuestion reportItems(ReportItems reportItems) {
        this.reportItems = reportItems;
        return this;
    }

    public void setReportItems(ReportItems reportItems) {
        this.reportItems = reportItems;
    }

    public Rectification getRectification() {
        return rectification;
    }

    public RemainsQuestion rectification(Rectification rectification) {
        this.rectification = rectification;
        return this;
    }

    public void setRectification(Rectification rectification) {
        this.rectification = rectification;
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
            "}";
    }
}
