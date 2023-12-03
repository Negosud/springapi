package fr.negosud.springapi.api.audit;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class AuditListener<T> {

    @PrePersist
    public void prePersist(Object object) {
        if (object instanceof AuditableEntity auditable) {
            auditable.onCreate();
        }
    }

    @PreUpdate
    public void preUpdate(Object object) {
        if (object instanceof AuditableEntity auditable) {
            auditable.onUpdate();
        }
    }
}
