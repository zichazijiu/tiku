package com.songzi.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Statistics.
 */
@Entity
@Table(name = "statistics")
public class Statistics implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "jhi_type", nullable = false)
    private String type;

    @NotNull
    @Column(name = "statistics_date", nullable = false)
    private LocalDate statisticsDate;

    @NotNull
    @Column(name = "update_date", nullable = false)
    private LocalDate updateDate;

    @NotNull
    @Column(name = "count", nullable = false)
    private Integer count;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public Statistics type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getStatisticsDate() {
        return statisticsDate;
    }

    public Statistics statisticsDate(LocalDate statisticsDate) {
        this.statisticsDate = statisticsDate;
        return this;
    }

    public void setStatisticsDate(LocalDate statisticsDate) {
        this.statisticsDate = statisticsDate;
    }

    public LocalDate getUpdateDate() {
        return updateDate;
    }

    public Statistics updateDate(LocalDate updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public void setUpdateDate(LocalDate updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getCount() {
        return count;
    }

    public Statistics count(Integer count) {
        this.count = count;
        return this;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public Statistics name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
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
        Statistics statistics = (Statistics) o;
        if (statistics.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), statistics.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Statistics{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", statisticsDate='" + getStatisticsDate() + "'" +
            ", updateDate='" + getUpdateDate() + "'" +
            ", count=" + getCount() +
            ", name='" + getName() + "'" +
            "}";
    }
}
