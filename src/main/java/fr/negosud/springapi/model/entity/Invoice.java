package fr.negosud.springapi.model.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import fr.negosud.springapi.model.entity.listener.AuditListener;
import fr.negosud.springapi.model.entity.audit.CreationAuditableEntity;
import fr.negosud.springapi.model.entity.annotation.AutoReference;
import fr.negosud.springapi.model.entity.constraint.ReferencedEntityConstraint;
import fr.negosud.springapi.model.entity.listener.ReferenceListener;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@EntityListeners({AuditListener.class, ReferenceListener.class})
@Table(name = "\"invoice\"")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "reference")
@AutoReference(referenceCode = "INVC")
public class Invoice extends CreationAuditableEntity implements ReferencedEntityConstraint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Column(length = 20, unique = true)
    private String reference;

    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    private Address address;

    @OneToOne
    @NotBlank
    @JsonIdentityReference(alwaysAsId = true)
    private Order order;

    public Invoice() { }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
