package com.songzi.web.rest.vm;

import com.songzi.domain.enumeration.BakType;
import com.songzi.domain.enumeration.Level;

import java.time.Instant;
import java.time.LocalDate;

public class DataBackupQueryVM {

    private LocalDate createdTime;

    private String description;

    private BakType bakType;

    public LocalDate getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDate createdTime) {
        this.createdTime = createdTime;
    }

    public BakType getBakType() {
        return bakType;
    }

    public void setBakType(BakType bakType) {
        this.bakType = bakType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
