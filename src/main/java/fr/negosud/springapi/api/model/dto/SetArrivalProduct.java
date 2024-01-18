package fr.negosud.springapi.api.model.dto;

import fr.negosud.springapi.api.model.entity.Arrival;
import fr.negosud.springapi.api.model.entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;

public class SetArrivalProduct {
    @Schema(description = "Arrival's product quantity")
    private int quantity;

    @Schema(description = "Arrival")
    private Arrival arrival;

    @Schema(description = "Arrival's product ")
    private Product product;

    public SetArrivalProduct() {
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Arrival getArrival() {
        return arrival;
    }

    public void setArrival(Arrival arrival) {
        this.arrival = arrival;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
