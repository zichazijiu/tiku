package com.songzi.service.dto;

import com.songzi.domain.Rectification;
import com.songzi.domain.RemainsQuestion;

import java.util.List;

/**
 * Created by Ke Qingyuan on 2019/3/14.
 */
public class CheckItemWithAnswerDTO {

    private String content;
    private String description;
    private String result;
    private List<RemainsQuestion> remainsQuestionList;
    private List<Rectification> rectificationList;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<RemainsQuestion> getRemainsQuestionList() {
        return remainsQuestionList;
    }

    public void setRemainsQuestionList(List<RemainsQuestion> remainsQuestionList) {
        this.remainsQuestionList = remainsQuestionList;
    }

    public List<Rectification> getRectificationList() {
        return rectificationList;
    }

    public void setRectificationList(List<Rectification> rectificationList) {
        this.rectificationList = rectificationList;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
