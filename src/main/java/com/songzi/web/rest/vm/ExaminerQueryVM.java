package com.songzi.web.rest.vm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "考评员列表查询模型")
public class ExaminerQueryVM {

    @ApiModelProperty(value = "考评官员姓名")
    private String name;

    @ApiModelProperty(value = "部门id")
    private Long departMentId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDepartMentId() {
        return departMentId;
    }

    public void setDepartMentId(Long departMentId) {
        this.departMentId = departMentId;
    }
}
