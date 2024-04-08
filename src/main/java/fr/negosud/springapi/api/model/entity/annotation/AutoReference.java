package fr.negosud.springapi.api.model.entity.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @implNote referenceCode() should return a 4 characters String
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoReference {
    String DEFAULT_REFERENCE_CODE = "DFLT";
    String referenceCode() default DEFAULT_REFERENCE_CODE;
}
