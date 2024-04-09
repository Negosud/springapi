package fr.negosud.springapi.model.dto.response.element;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import fr.negosud.springapi.model.entity.Arrival;
import jakarta.validation.constraints.NotBlank;

public class ArrivalProductInProductResponseElement {

    @NotBlank
    private long id;

    @NotBlank
    private int quantity;

    @JsonIdentityReference(alwaysAsId = true)
    private Arrival arrival;

    private ProductTransactionInProductResponseElement productTransaction;

    public ArrivalProductInProductResponseElement() { }

    public long getId() {
        return id;
    }

    public ArrivalProductInProductResponseElement setId(long id) {
        this.id = id;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public ArrivalProductInProductResponseElement setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public Arrival getArrival() {
        return arrival;
    }

    public ArrivalProductInProductResponseElement setArrival(Arrival arrival) {
        this.arrival = arrival;
        return this;
    }

    public ProductTransactionInProductResponseElement getProductTransaction() {
        return productTransaction;
    }

    public ArrivalProductInProductResponseElement setProductTransaction(ProductTransactionInProductResponseElement productTransaction) {
        this.productTransaction = productTransaction;
        return this;
    }
}
