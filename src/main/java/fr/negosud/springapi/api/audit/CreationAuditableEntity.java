package fr.negosud.springapi.api.audit;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import fr.negosud.springapi.api.component.ActionUserContextHolder;
import fr.negosud.springapi.api.model.entity.User;
import jakarta.persistence.*;

import java.util.Date;

@MappedSuperclass
public abstract class CreationAuditableEntity implements AuditableEntity {

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    protected Date createdAt;

    @ManyToOne
    @JoinColumn(name = "created_by", updatable = false)
    @JsonIdentityReference(alwaysAsId = true)
    protected User createdBy;

    @PrePersist
    public void onCreate() {
        this.createdAt = new Date();
        this.createdBy = ActionUserContextHolder.getActionUser();
    }

    @PreUpdate
    public void onUpdate() {

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
}
