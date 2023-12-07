package fr.negosud.springapi.api.audit;

import fr.negosud.springapi.api.model.entity.User;
import jakarta.persistence.*;

import java.util.Date;

public abstract class CreationAuditableEntity implements AuditableEntity {

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    protected Date createdAt;

    @ManyToOne
    @Column(name = "created_by", nullable = true, updatable = false)
    protected User createdBy;

    @PrePersist
    public void onCreate() {
        this.createdAt = new Date();
    }

    @PreUpdate
    public void onUpdate() {

    }

}
