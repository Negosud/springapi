package fr.negosud.springapi.api.model.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import fr.negosud.springapi.api.audit.AuditListener;
import fr.negosud.springapi.api.audit.CreationAuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@EntityListeners(AuditListener.class)
@Table(name = "\"invoice\"")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "reference")
public class Invoice extends CreationAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private long id;

    @NotBlank
    @Column(length = 20, unique = true)
    private String reference;

    @ManyToOne
    @NotBlank
    private Address address;

    @ManyToOne
    @NotBlank
    private Order order;

    public Invoice() { }

    public Invoice(long id, String reference, Address address, Order order) {
        this.id = id;
        this.reference = reference;
        this.address = address;
        this.order = order;
    }

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
