package com.songzi.service.dto;

import java.io.Serializable;
import java.time.Instant;

/**
 * @author Ke Bei
 * @date 2020/3/21
 */
public class DBBackupDTO implements Serializable {
    private Long id;
    private Instant createdTime;
    private String createdBy;
    private String description;
    private Integer size;
    private String filepath;
    private String authority;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
    @Override
    public String toString() {
        return "DBBackup{" +
            "id=" + getId() +
            ", createdTime='" + getCreatedTime() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", description='" + getDescription() + "'" +
            ", size=" + getSize() +
            ", filepath='" + getFilepath() + "'" +
            ", authority='" + getAuthority() + "'" +
            "}";
    }
}
