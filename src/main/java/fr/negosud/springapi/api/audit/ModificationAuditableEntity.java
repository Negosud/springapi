package fr.negosud.springapi.api.audit;

import fr.negosud.springapi.api.component.ActionUserContextHolder;
import fr.negosud.springapi.api.model.entity.User;
import jakarta.persistence.*;

import java.util.Date;

@MappedSuperclass
public abstract class ModificationAuditableEntity implements AuditableEntity {

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_at", nullable = false, updatable = false)
    protected Date modifiedAt;

    @ManyToOne
    @JoinColumn(name = "modified_by", updatable = false)
    protected User modifiedBy;

    @PrePersist
    public void onCreate() {
        this.modifiedAt = new Date();
        this.modifiedBy = ActionUserContextHolder.getActionUser() == null ? this.modifiedBy : ActionUserContextHolder.getActionUser();
    }

    @PreUpdate
    public void onUpdate() {
        this.modifiedAt = new Date();
        this.modifiedBy = ActionUserContextHolder.getActionUser() == null ? this.modifiedBy : ActionUserContextHolder.getActionUser();
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
