package com.songzi.domain;

/**
 * Created by Ke Qingyuan on 2019/3/3.
 */
public class CheckItemAnswerWithItem {
    private Long itemId;
    private Long answerId;
    private String yiliuProblem;
    private String zhenggai;
    private String answerUser;
    private String content;
    private String description;
    private String result;

    public CheckItemAnswerWithItem(Long itemId, String content,
                                   String description, Long answerId,
                                   String result, String yiliuProblem,
                                   String zhenggai,String answerUser) {
    super();
    this.itemId=itemId;
    this.content=content;
    this.description =description;
    this.answerId = answerId;
    this.result =result;
    this.yiliuProblem =yiliuProblem;
    this.zhenggai=zhenggai;
    this.answerUser =answerUser;
    }

    public String getYiliuProblem() {
        return yiliuProblem;
    }

    public void setYiliuProblem(String yiliuProblem) {
        this.yiliuProblem = yiliuProblem;
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
