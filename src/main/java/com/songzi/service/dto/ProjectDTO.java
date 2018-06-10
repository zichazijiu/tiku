package com.songzi.service.dto;

import io.swagger.models.auth.In;

import java.time.LocalDate;

public class ProjectDTO {

    private String name;

    private String description;

    private LocalDate date;

    private Integer score;

    public ProjectDTO(String name, String description, LocalDate date, Integer score){
        this.name = name;
        this.description = description;
        this.date = date;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
