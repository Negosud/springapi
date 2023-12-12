package fr.negosud.springapi.api.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private Long orderId;

    @NotBlank
    @Column(nullable = false, length = 20)
    private String reference;

    @NotBlank
    @Column(nullable = false, length = 20)
    private String status;

    @NotBlank
    @Column(nullable = false)
    private Date controlledAt;

    @NotBlank
    @Column(nullable = false)
    private Date closedAt;
    public Order() {
    }

    public Order(Long orderId, String reference, String status, Date controlledAt, Date closedAt) {
        this.orderId = orderId;
        this.reference = reference;
        this.status = status;
        this.controlledAt = controlledAt;
        this.closedAt = closedAt;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getControlledAt() {
        return controlledAt;
    }

    public void setControlledAt(Date controlledAt) {
        this.controlledAt = controlledAt;
    }

    public Date getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(Date closedAt) {
        this.closedAt = closedAt;
    }
}
