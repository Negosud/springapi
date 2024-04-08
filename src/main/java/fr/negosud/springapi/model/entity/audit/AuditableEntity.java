package fr.negosud.springapi.model.entity.audit;

public interface AuditableEntity {

    void onCreate();

    void onUpdate();
}
