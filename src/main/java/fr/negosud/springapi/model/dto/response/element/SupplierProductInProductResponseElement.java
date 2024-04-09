package fr.negosud.springapi.model.dto.response.element;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import fr.negosud.springapi.model.entity.User;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public class SupplierProductInProductResponseElement {

    @NotBlank
    private long id;

    @NotBlank
    private int quantity;

    @NotBlank
    private BigDecimal unitPrice;

    @NotBlank
    @JsonIdentityReference(alwaysAsId = true)
    private User supplier;

    public SupplierProductInProductResponseElement() { }

    public long getId() {
        return id;
    }

    public SupplierProductInProductResponseElement setId(long id) {
        this.id = id;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public SupplierProductInProductResponseElement setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public SupplierProductInProductResponseElement setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public User getSupplier() {
        return supplier;
    }

    public SupplierProductInProductResponseElement setSupplier(User supplier) {
        this.supplier = supplier;
        return this;
    }
}
