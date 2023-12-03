package fr.negosud.springapi.api.audit;

import fr.negosud.springapi.api.entity.User;
import jakarta.persistence.*;

import java.util.Date;

public abstract class ModificationAuditableEntity implements AuditableEntity {

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_at", nullable = false, updatable = false)
    protected Date modifiedAt;

    @ManyToOne
    @Column(name = "modified_by", nullable = true, updatable = false)
    protected User modifiedBy;

    @PrePersist
    public void onCreate() {
        this.modifiedAt = new Date();
    }

    @PreUpdate
    public void onUpdate() {
        this.modifiedAt = new Date();
    }
}
