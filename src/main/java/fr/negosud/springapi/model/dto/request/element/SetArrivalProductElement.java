package fr.negosud.springapi.model.dto.request.element;

import jakarta.validation.constraints.NotBlank;

public class SetArrivalProductElement {

    @NotBlank
    private long productId;

    @NotBlank
    private int quantity;

    public SetArrivalProductElement() { }

    public long getProductId() {
        return productId;
    }

    public SetArrivalProductElement setProductId(long productId) {
        this.productId = productId;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public SetArrivalProductElement setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }
}
