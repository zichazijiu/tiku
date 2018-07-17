package com.songzi.service.dto;

import java.util.List;

public class ProblemPercentDTO {

    private Integer totalRight;

    private List<SubjectPercentDTO> subjectPercentDTOList;

    public Integer getTotalRight() {
        return totalRight;
    }

    public void setTotalRight(Integer totalRight) {
        this.totalRight = totalRight;
    }

    public List<SubjectPercentDTO> getSubjectPercentDTOList() {
        return subjectPercentDTOList;
    }

    public void setSubjectPercentDTOList(List<SubjectPercentDTO> subjectPercentDTOList) {
        this.subjectPercentDTOList = subjectPercentDTOList;
    }
}
