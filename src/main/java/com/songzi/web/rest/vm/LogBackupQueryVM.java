package com.songzi.web.rest.vm;

import com.songzi.domain.enumeration.Level;

import java.time.LocalDate;
import java.time.LocalTime;

public class LogBackupQueryVM {

    private LocalDate createdStartDate;

    private String createdStartTime;

    private LocalDate createdEndDate;

    private String createdEndTime;

    private Level level;

    private String description;

    public LocalDate getCreatedStartDate() {
        return createdStartDate;
    }

    public void setCreatedStartDate(LocalDate createdStartDate) {
        this.createdStartDate = createdStartDate;
    }

    public String getCreatedStartTime() {
        return createdStartTime;
    }

    public void setCreatedStartTime(String createdStartTime) {
        this.createdStartTime = createdStartTime;
    }

    public LocalDate getCreatedEndDate() {
        return createdEndDate;
    }

    public void setCreatedEndDate(LocalDate createdEndDate) {
        this.createdEndDate = createdEndDate;
    }

    public String getCreatedEndTime() {
        return createdEndTime;
    }

    public void setCreatedEndTime(String createdEndTime) {
        this.createdEndTime = createdEndTime;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
