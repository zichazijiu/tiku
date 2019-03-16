package com.songzi.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Rectification.
 */
@Entity
@Table(name = "rectification")
public class Rectification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "measure")
    private String measure;

    @Column(name = "result")
    private String result;

    @NotNull
    @Column(name = "rectification_time", nullable = false)
    private ZonedDateTime rectificationTime;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private RemainsQuestion remainsQuestion;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMeasure() {
        return measure;
    }

    public Rectification measure(String measure) {
        this.measure = measure;
        return this;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getResult() {
        return result;
    }

    public Rectification result(String result) {
        this.result = result;
        return this;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ZonedDateTime getRectificationTime() {
        return rectificationTime;
    }

    public Rectification rectificationTime(ZonedDateTime rectificationTime) {
        this.rectificationTime = rectificationTime;
        return this;
    }

    public void setRectificationTime(ZonedDateTime rectificationTime) {
        this.rectificationTime = rectificationTime;
    }

    @JsonIgnore
    public RemainsQuestion getRemainsQuestion() {
        return remainsQuestion;
    }

    public Rectification remainsQuestion(RemainsQuestion remainsQuestion) {
        this.remainsQuestion = remainsQuestion;
        return this;
    }

    public void setRemainsQuestion(RemainsQuestion remainsQuestion) {
        this.remainsQuestion = remainsQuestion;
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
        Rectification rectification = (Rectification) o;
        if (rectification.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rectification.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Rectification{" +
            "id=" + getId() +
            ", measure='" + getMeasure() + "'" +
            ", result='" + getResult() + "'" +
            ", rectificationTime='" + getRectificationTime() + "'" +
            "}";
    }
}
