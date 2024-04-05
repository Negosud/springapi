package fr.negosud.springapi.api.model.dto.request.element;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public class SetUsersSuppliedProductElement {

    @NotBlank
    private int quantity;

    @NotBlank
    @Column(precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @NotBlank
    private long productId;

    public SetUsersSuppliedProductElement() { }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }
}
