package com.songzi.web.rest.vm;

public class QuestionVM {

    private Long subjectId;

    private Long right;

    private String[] multipleChoiceAnswers;

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public Long getRight() {
        return right;
    }

    public void setRight(Long right) {
        this.right = right;
    }

    public String[] getMultipleChoiceAnswers() {
        return multipleChoiceAnswers;
    }

    public void setMultipleChoiceAnswers(String[] multipleChoiceAnswers) {
        this.multipleChoiceAnswers = multipleChoiceAnswers;
    }
}
