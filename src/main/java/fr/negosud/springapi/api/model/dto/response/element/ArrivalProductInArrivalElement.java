package fr.negosud.springapi.api.model.dto.response.element;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import fr.negosud.springapi.api.model.entity.Product;
import jakarta.validation.constraints.NotBlank;

public class ArrivalProductInArrivalElement {

    private long id;

    @NotBlank
    private int quantity;

    @NotBlank
    @JsonIdentityReference(alwaysAsId = true)
    private Product product;

    public ArrivalProductInArrivalElement() { }

    public long getId() {
        return id;
    }

    public ArrivalProductInArrivalElement setId(long id) {
        this.id = id;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public ArrivalProductInArrivalElement setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public Product getProduct() {
        return product;
    }

    public ArrivalProductInArrivalElement setProduct(Product product) {
        this.product = product;
        return this;
    }
}
