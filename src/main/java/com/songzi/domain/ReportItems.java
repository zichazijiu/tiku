package com.songzi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ReportItems.
 */
@Entity
@Table(name = "report_items")
public class ReportItems implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_level")
    private String level;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnore
    @JsonIgnoreProperties
    private Report report;

    @OneToOne
    @JoinColumn
    private CheckItem checkItem;

    @OneToMany(mappedBy = "reportItems", fetch = FetchType.EAGER)
    private Set<RemainsQuestion> remainsQuestions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public ReportItems level(String level) {
        this.level = level;
        return this;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Report getReport() {
        return report;
    }

    public ReportItems report(Report report) {
        this.report = report;
        return this;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public CheckItem getCheckItem() {
        return checkItem;
    }

    public ReportItems checkItem(CheckItem checkItem) {
        this.checkItem = checkItem;
        return this;
    }

    public void setCheckItem(CheckItem checkItem) {
        this.checkItem = checkItem;
    }

    public Set<RemainsQuestion> getRemainsQuestions() {
        return remainsQuestions;
    }

    public ReportItems remainsQuestions(Set<RemainsQuestion> remainsQuestions) {
        this.remainsQuestions = remainsQuestions;
        return this;
    }

    public ReportItems addRemainsQuestion(RemainsQuestion remainsQuestion) {
        this.remainsQuestions.add(remainsQuestion);
        remainsQuestion.setReportItems(this);
        return this;
    }

    public ReportItems removeRemainsQuestion(RemainsQuestion remainsQuestion) {
        this.remainsQuestions.remove(remainsQuestion);
        remainsQuestion.setReportItems(null);
        return this;
    }

    public void setRemainsQuestions(Set<RemainsQuestion> remainsQuestions) {
        this.remainsQuestions = remainsQuestions;
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
        ReportItems reportItems = (ReportItems) o;
        if (reportItems.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reportItems.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReportItems{" +
            "id=" + getId() +
            ", level='" + getLevel() + "'" +
            "}";
    }
}
