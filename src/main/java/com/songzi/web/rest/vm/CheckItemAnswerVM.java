package com.songzi.web.rest.vm;

import java.util.List;

/**
 * Created by Ke Qingyuan on 2019/3/3.
 */
public class CheckItemAnswerVM {
    private String itemId;
    private String answerId;
    private List<YiliuProblem> yiliuProblemList;
    private String zhenggai;
    private String answerUser;
    private String content;
    private String description;
    private String result;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public List<YiliuProblem> getYiliuProblemList() {
        return yiliuProblemList;
    }

    public void setYiliuProblemList(List<YiliuProblem> yiliuProblemList) {
        this.yiliuProblemList = yiliuProblemList;
    }

    public String getZhenggai() {
        return zhenggai;
    }

    public void setZhenggai(String zhenggai) {
        this.zhenggai = zhenggai;
    }

    public String getAnswerUser() {
        return answerUser;
    }

    public void setAnswerUser(String answerUser) {
        this.answerUser = answerUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

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
}
