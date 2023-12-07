package fr.negosud.springapi.api.audit;

interface AuditableEntity {

    void onCreate();

    void onUpdate();

}
