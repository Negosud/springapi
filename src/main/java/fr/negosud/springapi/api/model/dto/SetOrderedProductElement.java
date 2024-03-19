package fr.negosud.springapi.api.model.dto;

import jakarta.validation.constraints.NotBlank;

public class SetOrderedProductElement {

    @NotBlank
    private long productId;

    @NotBlank
    private int quantity;

    public SetOrderedProductElement() { }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
