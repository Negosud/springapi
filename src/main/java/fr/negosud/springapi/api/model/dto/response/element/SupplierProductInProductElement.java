package fr.negosud.springapi.api.model.dto.response.element;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import fr.negosud.springapi.api.model.entity.User;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public class SupplierProductInProductElement {

    private long id;

    @NotBlank
    private int quantity;

    @NotBlank
    @Column(precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @NotBlank
    @JsonIdentityReference(alwaysAsId = true)
    private User supplier;

    public SupplierProductInProductElement() { }

    public long getId() {
        return id;
    }

    public SupplierProductInProductElement setId(long id) {
        this.id = id;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public SupplierProductInProductElement setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public SupplierProductInProductElement setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public User getSupplier() {
        return supplier;
    }

    public SupplierProductInProductElement setSupplier(User supplier) {
        this.supplier = supplier;
        return this;
    }
}
