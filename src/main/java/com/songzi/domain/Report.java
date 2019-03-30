package com.songzi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.songzi.domain.enumeration.ReportStatus;

/**
 * A Report.
 */
@Entity
@Table(name = "report")
public class Report implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "created_time", nullable = false)
    private ZonedDateTime createdTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "report_status", nullable = false)
    private ReportStatus reportStatus;

    @Column(name = "jhi_level")
    private String level;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "report", cascade = {CascadeType.REMOVE})
    @JsonIgnore
    private Set<ReportItems> reportItems = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreatedTime() {
        return createdTime;
    }

    public Report createdTime(ZonedDateTime createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    public void setCreatedTime(ZonedDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public ReportStatus getReportStatus() {
        return reportStatus;
    }

    public Report reportStatus(ReportStatus reportStatus) {
        this.reportStatus = reportStatus;
        return this;
    }

    public void setReportStatus(ReportStatus reportStatus) {
        this.reportStatus = reportStatus;
    }

    public String getLevel() {
        return level;
    }

    public Report level(String level) {
        this.level = level;
        return this;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public User getUser() {
        return user;
    }

    public Report user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<ReportItems> getReportItems() {
        return reportItems;
    }

    public Report reportItems(Set<ReportItems> reportItems) {
        this.reportItems = reportItems;
        return this;
    }

    public Report addReportItems(ReportItems reportItems) {
        this.reportItems.add(reportItems);
        reportItems.setReport(this);
        return this;
    }

    public Report removeReportItems(ReportItems reportItems) {
        this.reportItems.remove(reportItems);
        reportItems.setReport(null);
        return this;
    }

    public void setReportItems(Set<ReportItems> reportItems) {
        this.reportItems = reportItems;
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
        Report report = (Report) o;
        if (report.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), report.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Report{" +
            "id=" + getId() +
            ", createdTime='" + getCreatedTime() + "'" +
            ", reportStatus='" + getReportStatus() + "'" +
            ", level='" + getLevel() + "'" +
            "}";
    }
}
