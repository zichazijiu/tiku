package com.songzi.service.dto;

public class WeaknessDTO {

    private ProblemPercentDTO problemPercentDTO;

    private AnswerTimePercentDTO answerTimePercentDTO;

    public ProblemPercentDTO getProblemPercentDTO() {
        return problemPercentDTO;
    }

    public void setProblemPercentDTO(ProblemPercentDTO problemPercentDTO) {
        this.problemPercentDTO = problemPercentDTO;
    }

    public AnswerTimePercentDTO getAnswerTimePercentDTO() {
        return answerTimePercentDTO;
    }

    public void setAnswerTimePercentDTO(AnswerTimePercentDTO answerTimePercentDTO) {
        this.answerTimePercentDTO = answerTimePercentDTO;
    }
}
