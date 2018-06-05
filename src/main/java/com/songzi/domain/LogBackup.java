package com.songzi.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import com.songzi.domain.enumeration.Level;

/**
 * A LogBackup.
 */
@Entity
@Table(name = "log_backup")
public class LogBackup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "created_time", nullable = false)
    private Instant createdTime;

    @NotNull
    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "jhi_size")
    private Integer size;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_level")
    private Level level;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }

    public LogBackup createdTime(Instant createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public LogBackup createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getDescription() {
        return description;
    }

    public LogBackup description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSize() {
        return size;
    }

    public LogBackup size(Integer size) {
        this.size = size;
        return this;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Level getLevel() {
        return level;
    }

    public LogBackup level(Level level) {
        this.level = level;
        return this;
    }

    public void setLevel(Level level) {
        this.level = level;
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
        LogBackup logBackup = (LogBackup) o;
        if (logBackup.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), logBackup.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LogBackup{" +
            "id=" + getId() +
            ", createdTime='" + getCreatedTime() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", description='" + getDescription() + "'" +
            ", size=" + getSize() +
            ", level='" + getLevel() + "'" +
            "}";
    }
}
