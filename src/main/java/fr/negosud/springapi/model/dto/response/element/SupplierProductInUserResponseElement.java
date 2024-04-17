package fr.negosud.springapi.model.dto.response.element;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import fr.negosud.springapi.model.entity.Product;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public class SupplierProductInUserResponseElement {

    @NotBlank
    private int quantity;

    @NotBlank
    private BigDecimal unitPrice;

    @NotBlank
    @JsonIdentityReference(alwaysAsId = true)
    private Product product;

    public SupplierProductInUserResponseElement() { }

    public int getQuantity() {
        return quantity;
    }

    public SupplierProductInUserResponseElement setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public SupplierProductInUserResponseElement setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public Product getProduct() {
        return product;
    }

    public SupplierProductInUserResponseElement setProduct(Product product) {
        this.product = product;
        return this;
    }
}
