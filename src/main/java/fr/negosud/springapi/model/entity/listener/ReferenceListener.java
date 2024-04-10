package fr.negosud.springapi.model.entity.listener;

import fr.negosud.springapi.model.entity.annotation.AutoReference;
import fr.negosud.springapi.model.entity.constraint.ReferencedEntityConstraint;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PostPersist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

import static java.lang.System.out;

@Component
public class ReferenceListener<T> {

    private static EntityManager entityManager;

    @Autowired
    public void setEntityManagerFactory(@Lazy EntityManagerFactory entityManagerFactory) {
        entityManager = entityManagerFactory.createEntityManager();
    }

    @PostPersist
    public void autoSetReference(Object object) {
        if (object instanceof ReferencedEntityConstraint referencedEntity && referencedEntity.getReference() == null) {
            Class<?> entityClass = referencedEntity.getClass();
            String entityCode;
            AutoReference autoReferenceAnnotation = entityClass.getAnnotation(AutoReference.class);
            entityCode = autoReferenceAnnotation != null ?
                    autoReferenceAnnotation.referenceCode() :
                    AutoReference.DEFAULT_REFERENCE_CODE;
            String reference = entityCode + "_" + new SimpleDateFormat("ddMMyyyy").format(referencedEntity.getCreatedAt()) + "_" + String.format("%6s", referencedEntity.getId()).replace(' ', '0');
            referencedEntity.setReference(reference);

            entityManager.merge(referencedEntity);
            out.println("Nouvelle référence ajoutée : " + reference);
        }
    }
}
