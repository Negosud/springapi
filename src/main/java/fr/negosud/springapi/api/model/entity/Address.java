package fr.negosud.springapi.api.model.entity;

import fr.negosud.springapi.api.audit.AuditListener;
import fr.negosud.springapi.api.audit.FullAuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@EntityListeners(AuditListener.class)
final public class Address extends FullAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private Long addressId;

    @Column(length = 100)
    private String country;

    @Column(length = 100)
    private String administrativeDivision;

    @Column(length = 100)
    private String city;

    @Column(length = 10)
    private String postalCode;

    private String addressLine1;

    private String addressLine2;

    private boolean active;

}
