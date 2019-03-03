package com.songzi.service.dto;

import com.alibaba.fastjson.JSON;
import com.songzi.web.rest.vm.YiliuProblem;

import java.util.List;

/**
 * Created by Ke Qingyuan on 2019/3/3.
 */
public class CheckItemAnswerDTO {

    private Long itemId;
    private Long answerId;
    private String zhenggai;
    private String answerUser;
    private String content;
    private String description;
    private String result;
    private List<YiliuProblem> yiliuProblemList;

    public CheckItemAnswerDTO(){}

    public CheckItemAnswerDTO(Long itemId, Long answerId, String yiliuProblem, String zhenggai, String answerUser, String content, String description, String result) {
    super();
    this.itemId=itemId;
    this.answerId=answerId;
    this.zhenggai=zhenggai;
    this.answerUser = answerUser;
    this.content=content;
    this.description=description;
    this.result=result;
    this.yiliuProblemList = JSON.parseArray(yiliuProblem,YiliuProblem.class);
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

    public List<YiliuProblem> getYiliuProblemList() {
        return yiliuProblemList;
    }

    public void setYiliuProblemList(List<YiliuProblem> yiliuProblemList) {
        this.yiliuProblemList = yiliuProblemList;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }
}
