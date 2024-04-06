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

    public void setId(long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
