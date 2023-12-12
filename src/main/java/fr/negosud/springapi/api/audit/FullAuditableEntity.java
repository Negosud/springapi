package fr.negosud.springapi.api.audit;

import fr.negosud.springapi.api.component.ActionUserContextHolder;
import fr.negosud.springapi.api.model.entity.User;
import jakarta.persistence.*;

import java.util.Date;

public abstract class FullAuditableEntity implements AuditableEntity {

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    protected Date createdAt;

    @ManyToOne
    @Column(name = "created_by", updatable = false)
    protected User createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_at", nullable = false, updatable = false)
    protected Date modifiedAt;

    @ManyToOne
    @Column(name = "modified_by", updatable = false)
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

}
