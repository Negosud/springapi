package fr.negosud.springapi.model.entity.audit;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import fr.negosud.springapi.component.ActionUserContextHolder;
import fr.negosud.springapi.model.entity.User;
import jakarta.persistence.*;

import java.util.Date;

@MappedSuperclass
public abstract class FullAuditableEntity implements AuditableEntity {

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    protected Date createdAt;

    @ManyToOne
    @JoinColumn(name = "created_by")
    @JsonIdentityReference(alwaysAsId = true)
    protected User createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_at", nullable = false)
    protected Date modifiedAt;

    @ManyToOne
    @JoinColumn(name = "modified_by")
    @JsonIdentityReference(alwaysAsId = true)
    protected User modifiedBy;

    @PrePersist
    public void onCreate() {
        this.createdAt = new Date();
        this.modifiedAt = new Date();
        this.createdBy = ActionUserContextHolder.getActionUser();
        this.modifiedBy = ActionUserContextHolder.getActionUser() == null ? this.modifiedBy : ActionUserContextHolder.getActionUser();
    }

    @PreUpdate
    public void onUpdate() {
        this.modifiedAt = new Date();
        this.modifiedBy = ActionUserContextHolder.getActionUser() == null ? this.modifiedBy : ActionUserContextHolder.getActionUser();
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public User getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
}
