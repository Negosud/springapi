package fr.negosud.springapi.model.dto.response.element;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import fr.negosud.springapi.model.entity.Arrival;
import jakarta.validation.constraints.NotBlank;

public class ArrivalProductInProductElement {

    @NotBlank
    private long id;

    @NotBlank
    private int quantity;

    @JsonIdentityReference(alwaysAsId = true)
    private Arrival arrival;

    private ProductTransactionInProductElement productTransaction;

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

    public ProductTransactionInProductElement getProductTransaction() {
        return productTransaction;
    }

    public ArrivalProductInProductElement setProductTransaction(ProductTransactionInProductElement productTransaction) {
        this.productTransaction = productTransaction;
        return this;
    }
}
