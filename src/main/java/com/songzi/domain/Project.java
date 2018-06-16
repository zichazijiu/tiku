package com.songzi.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.songzi.domain.enumeration.DeleteFlag;

import com.songzi.domain.enumeration.Status;

import com.songzi.domain.enumeration.Type;
import org.hibernate.annotations.BatchSize;

/**
 * A Project.
 */
@Entity
@Table(name = "project")
public class Project extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "del_flag", nullable = false)
    private DeleteFlag delFlag;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "project_type")
    private Type type;

    @NotNull
    @Column(name = "duration", nullable = false)
    private Integer duration;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "project_subject",
        joinColumns = {@JoinColumn(name = "project_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "subject_id", referencedColumnName = "id")})

    @BatchSize(size = 20)
    private Set<Subject> subjects = new HashSet<>();

    @OneToMany(mappedBy = "project")
    @JsonIgnore
    private Set<Examine> examines = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Project name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Project description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DeleteFlag getDelFlag() {
        return delFlag;
    }

    public Project delFlag(DeleteFlag delFlag) {
        this.delFlag = delFlag;
        return this;
    }

    public void setDelFlag(DeleteFlag delFlag) {
        this.delFlag = delFlag;
    }

    public Status getStatus() {
        return status;
    }

    public Project status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Type getType() {
        return type;
    }

    public Project type(Type type) {
        this.type = type;
        return this;
    }

    public void setType(Type type) {
        this.type = type;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove


    public Set<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }


    public Set<Examine> getExamines() {
        return examines;
    }

    public Project examines(Set<Examine> examines) {
        this.examines = examines;
        return this;
    }

    public Project addExamine(Examine examine) {
        this.examines.add(examine);
        examine.setProject(this);
        return this;
    }

    public Project removeExamine(Examine examine) {
        this.examines.remove(examine);
        examine.setProject(null);
        return this;
    }

    public void setExamines(Set<Examine> examines) {
        this.examines = examines;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Project project = (Project) o;
        if (project.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), project.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Project{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", delFlag='" + getDelFlag() + "'" +
            ", status='" + getStatus() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
