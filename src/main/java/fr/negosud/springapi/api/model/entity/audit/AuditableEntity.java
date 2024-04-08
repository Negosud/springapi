package fr.negosud.springapi.api.model.entity.audit;

public interface AuditableEntity {

    void onCreate();

    void onUpdate();
}
