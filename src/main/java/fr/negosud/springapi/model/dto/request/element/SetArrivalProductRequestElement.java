package fr.negosud.springapi.model.dto.request.element;

import jakarta.validation.constraints.NotBlank;

public class SetArrivalProductRequestElement {

    @NotBlank
    private long productId;

    @NotBlank
    private int quantity;

    public SetArrivalProductRequestElement() { }

    public long getProductId() {
        return productId;
    }

    public SetArrivalProductRequestElement setProductId(long productId) {
        this.productId = productId;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public SetArrivalProductRequestElement setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }
}
