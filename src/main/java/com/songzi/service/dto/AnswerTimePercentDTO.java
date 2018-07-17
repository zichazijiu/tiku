package com.songzi.service.dto;

import java.util.List;

public class AnswerTimePercentDTO {

    private Integer totalMinutes;

    private List<TimePercentDTO> timePercentDTOList;

    public Integer getTotalMinutes() {
        return totalMinutes;
    }

    public void setTotalMinutes(Integer totalMinutes) {
        this.totalMinutes = totalMinutes;
    }

    public List<TimePercentDTO> getTimePercentDTOList() {
        return timePercentDTOList;
    }

    public void setTimePercentDTOList(List<TimePercentDTO> timePercentDTOList) {
        this.timePercentDTOList = timePercentDTOList;
    }
}
