package com.songzi.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A ReleaseHistory.
 */
@Entity
@Table(name = "check_item_release_history")
public class ReleaseHistory extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "release_id")
    private Long releaseId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReleaseId() {
        return releaseId;
    }

    public ReleaseHistory releaseId(Long releaseId) {
        this.releaseId = releaseId;
        return this;
    }

    public void setReleaseId(Long releaseId) {
        this.releaseId = releaseId;
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
        ReleaseHistory releaseHistory = (ReleaseHistory) o;
        if (releaseHistory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), releaseHistory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReleaseHistory{" +
            "id=" + getId() +
            ", releaseId=" + getReleaseId() +
            "}";
    }
}
