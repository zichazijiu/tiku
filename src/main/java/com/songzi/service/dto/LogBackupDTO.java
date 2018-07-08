package com.songzi.service.dto;


import com.songzi.domain.enumeration.BakType;
import com.songzi.domain.enumeration.Level;
import com.songzi.domain.enumeration.LogType;

import java.io.Serializable;
import java.time.Instant;

public class LogBackupDTO implements Serializable {

    private Long id;

    private Instant createdTime;

    private String createdBy;

    private String description;

    private Integer size;

    private Level level;

    private String authority;

    private LogType logType;

    private BakType bakType;

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

    public LogBackupDTO createdTime(Instant createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public LogBackupDTO createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getDescription() {
        return description;
    }

    public LogBackupDTO description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSize() {
        return size;
    }

    public LogBackupDTO size(Integer size) {
        this.size = size;
        return this;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Level getLevel() {
        return level;
    }

    public LogBackupDTO level(Level level) {
        this.level = level;
        return this;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public String getAuthority() {
        return authority;
    }

    public LogBackupDTO authority(String authority) {
        this.authority = authority;
        return this;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public LogType getLogType() {
        return logType;
    }

    public LogBackupDTO logType(LogType logType) {
        this.logType = logType;
        return this;
    }

    public void setLogType(LogType logType) {
        this.logType = logType;
    }

    public BakType getBakType() {
        return bakType;
    }

    public void setBakType(BakType bakType) {
        this.bakType = bakType;
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
            ", authority='" + getAuthority() + "'" +
            ", logType='" + getLogType() + "'" +
            "}";
    }
}
