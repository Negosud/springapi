package fr.negosud.springapi.api.model.constraint;

import java.util.Date;

public interface ReferencedEntityConstraint {
    String getReference();
    void setReference(String reference);
    long getId();
    Date getCreatedAt();
}
