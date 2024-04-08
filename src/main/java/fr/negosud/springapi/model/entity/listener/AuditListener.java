package fr.negosud.springapi.model.entity.listener;

import fr.negosud.springapi.model.entity.audit.AuditableEntity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import static java.lang.System.out;

public class AuditListener<T> {

    @PrePersist
    public void prePersist(Object object) {
        out.println("Listener trigger OnCreate for entity : ");
        out.println(object.getClass());
        out.println(object);
        if (object instanceof AuditableEntity auditable) {
            auditable.onCreate();
        }
    }

    @PreUpdate
    public void preUpdate(Object object) {
        out.println("Listener trigger OnUpdate for entity : ");
        out.println(object.getClass());
        out.println(object);
        if (object instanceof AuditableEntity auditable) {
            auditable.onUpdate();
        }
    }
}
