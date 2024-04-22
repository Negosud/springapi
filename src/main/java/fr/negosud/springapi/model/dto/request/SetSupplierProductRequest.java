package fr.negosud.springapi.model.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public class SetSupplierProductRequest {

    @NotBlank
    private Long supplierId;

    @NotBlank
    private Long productId;

    @NotBlank
    private int quantity;

    @NotBlank
    private BigDecimal unitPrice;

    public SetSupplierProductRequest() { }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

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
}
