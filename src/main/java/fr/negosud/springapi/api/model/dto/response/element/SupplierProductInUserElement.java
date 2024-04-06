package fr.negosud.springapi.api.model.dto.response.element;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import fr.negosud.springapi.api.model.entity.Product;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public class SupplierProductInUserElement {

    private long id;

    @NotBlank
    private int quantity;

    @NotBlank
    @Column(precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @NotBlank
    @JsonIdentityReference(alwaysAsId = true)
    private Product product;

    public SupplierProductInUserElement() { }

    public long getId() {
        return id;
    }

    public SupplierProductInUserElement setId(long id) {
        this.id = id;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public SupplierProductInUserElement setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public SupplierProductInUserElement setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public Product getProduct() {
        return product;
    }

    public SupplierProductInUserElement setProduct(Product product) {
        this.product = product;
        return this;
    }
}
