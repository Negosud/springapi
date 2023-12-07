package fr.negosud.springapi.api.audit;

import fr.negosud.springapi.api.model.entity.User;
import jakarta.persistence.*;

import java.util.Date;

// TODO: Implement user auditing (created_by + modified_by)

public abstract class FullAuditableEntity implements AuditableEntity {

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    protected Date createdAt;

    @ManyToOne
    @Column(name = "created_by", nullable = true, updatable = false)
    protected User createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_at", nullable = false, updatable = false)
    protected Date modifiedAt;

    @ManyToOne
    @Column(name = "modified_by", nullable = true, updatable = false)
    protected User modifiedBy;

    @PrePersist
    public void onCreate() {
        this.createdAt = new Date();
        this.modifiedAt = new Date();
    }

    @PreUpdate
    public void onUpdate() {
        this.modifiedAt = new Date();
    }

}
