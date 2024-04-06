package fr.negosud.springapi.api.model.dto.response.element;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import fr.negosud.springapi.api.model.entity.Arrival;
import jakarta.validation.constraints.NotBlank;

public class ArrivalProductInProductElement {

    private long id;

    @NotBlank
    private int quantity;

    @JsonIdentityReference(alwaysAsId = true)
    private Arrival arrival;

    public ArrivalProductInProductElement() { }

    public long getId() {
        return id;
    }

    public ArrivalProductInProductElement setId(long id) {
        this.id = id;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public ArrivalProductInProductElement setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public Arrival getArrival() {
        return arrival;
    }

    public ArrivalProductInProductElement setArrival(Arrival arrival) {
        this.arrival = arrival;
        return this;
    }
}
