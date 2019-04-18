package com.songzi.service.dto;

import com.songzi.domain.enumeration.ReleaseStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.Instant;

/**
 * Created by Ke Qingyuan on 2019/4/18.
 */
@ApiModel("发布")
public class ReleaseDTO {

    @ApiModelProperty(value = "发布编号")
    private Long id;

    @ApiModelProperty(value = "自评项目编号")
    private String checkItemIds;

    @ApiModelProperty(value = "发布状态")
    private ReleaseStatus releaseStatus;

    @ApiModelProperty(value = "发布名称")
    private String releaseName;

    @ApiModelProperty(value = "发布描述")
    private String releaseDescription;

    @ApiModelProperty(value = "创建时间")
    private Instant createdDate;

    @ApiModelProperty(value = "创建人")
    private String createdBy;

    @ApiModelProperty(value = "更新时间")
    private Instant lastModifiedDate;

    @ApiModelProperty(value = "创建人")
    private String lastModifiedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCheckItemIds() {
        return checkItemIds;
    }

    public void setCheckItemIds(String checkItemIds) {
        this.checkItemIds = checkItemIds;
    }

    public ReleaseStatus getReleaseStatus() {
        return releaseStatus;
    }

    public void setReleaseStatus(ReleaseStatus releaseStatus) {
        this.releaseStatus = releaseStatus;
    }

    public String getReleaseName() {
        return releaseName;
    }

    public void setReleaseName(String releaseName) {
        this.releaseName = releaseName;
    }

    public String getReleaseDescription() {
        return releaseDescription;
    }

    public void setReleaseDescription(String releaseDescription) {
        this.releaseDescription = releaseDescription;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
