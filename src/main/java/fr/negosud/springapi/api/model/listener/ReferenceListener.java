package fr.negosud.springapi.api.model.listener;

import jakarta.persistence.PostPersist;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.System.out;

public class ReferenceListener<T> {

    @PostPersist
    public void postPersist(Object object) {
        try {
            // TODO : reference should be null before setting it and field/method should be checked
            Method setterReference = object.getClass().getMethod("setReference", String.class);
            Method getterId = object.getClass().getMethod("getId");
            Method getterCreatedAt = object.getClass().getMethod("getCreatedAt");
            long id = (long) getterId.invoke(object);
            Date createdAt = (Date) getterCreatedAt.invoke(object);
            String reference = object.getClass().getName().substring(0, 4).toUpperCase() + "_" + new SimpleDateFormat("ddMMyyyy").format(createdAt) + "_" + String.format("%6s", id).replace(' ', '0');
            setterReference.invoke(object, reference);
            out.println("Nouvelle référence ajoutée : " + reference);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
