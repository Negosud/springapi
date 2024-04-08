package fr.negosud.springapi.api.model.dto.request.element;

import jakarta.validation.constraints.NotBlank;

public class SetOrderProductElement {

    @NotBlank
    private long productId;

    @NotBlank
    private int quantity;

    public SetOrderProductElement() { }

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
