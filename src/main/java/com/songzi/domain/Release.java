package com.songzi.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

import com.songzi.domain.enumeration.ReleaseStatus;

/**
 * A Release.
 */
@Entity
@Table(name = "check_item_release")
public class Release extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "check_item_ids")
    private String checkItemIds;

    @Enumerated(EnumType.STRING)
    @Column(name = "release_status")
    private ReleaseStatus releaseStatus;

    @Column(name = "release_name")
    private String releaseName;

    @Column(name = "release_description")
    private String releaseDescription;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCheckItemIds() {
        return checkItemIds;
    }

    public Release checkItemIds(String checkItemIds) {
        this.checkItemIds = checkItemIds;
        return this;
    }

    public void setCheckItemIds(String checkItemIds) {
        this.checkItemIds = checkItemIds;
    }

    public ReleaseStatus getReleaseStatus() {
        return releaseStatus;
    }

    public Release releaseStatus(ReleaseStatus releaseStatus) {
        this.releaseStatus = releaseStatus;
        return this;
    }

    public void setReleaseStatus(ReleaseStatus releaseStatus) {
        this.releaseStatus = releaseStatus;
    }

    public String getReleaseName() {
        return releaseName;
    }

    public Release releaseName(String releaseName) {
        this.releaseName = releaseName;
        return this;
    }

    public void setReleaseName(String releaseName) {
        this.releaseName = releaseName;
    }

    public String getReleaseDescription() {
        return releaseDescription;
    }

    public Release releaseDescription(String releaseDescription) {
        this.releaseDescription = releaseDescription;
        return this;
    }

    public void setReleaseDescription(String releaseDescription) {
        this.releaseDescription = releaseDescription;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Release release = (Release) o;
        if (release.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), release.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Release{" +
            "id=" + getId() +
            ", checkItemIds='" + getCheckItemIds() + "'" +
            ", releaseStatus='" + getReleaseStatus() + "'" +
            ", releaseName='" + getReleaseName() + "'" +
            ", releaseDescription='" + getReleaseDescription() + "'" +
            "}";
    }
}
