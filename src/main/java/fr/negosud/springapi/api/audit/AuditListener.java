package fr.negosud.springapi.api.audit;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class AuditListener<T> {

    @PrePersist
    public void prePersist(Object object) {
        System.out.println("Listener trigger OnCreate for entity : ");
        System.out.println(object.getClass());
        System.out.println(object);
        if (object instanceof AuditableEntity auditable) {
            auditable.onCreate();
        }
    }

    @PreUpdate
    public void preUpdate(Object object) {
        System.out.println("Listener trigger OnUpdate for entity : ");
        System.out.println(object.getClass());
        System.out.println(object);
        if (object instanceof AuditableEntity auditable) {
            auditable.onUpdate();
        }
    }
}
