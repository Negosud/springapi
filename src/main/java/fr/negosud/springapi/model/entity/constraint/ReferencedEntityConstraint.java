package fr.negosud.springapi.model.entity.constraint;

import java.util.Date;

public interface ReferencedEntityConstraint {
    String getReference();
    void setReference(String reference);
    long getId();
    Date getCreatedAt();
}
