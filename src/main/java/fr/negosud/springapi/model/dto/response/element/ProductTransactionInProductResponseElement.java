package fr.negosud.springapi.model.dto.response.element;

import fr.negosud.springapi.model.dto.response.audit.FullAuditedResponse;
import jakarta.validation.constraints.NotBlank;

public class ProductTransactionInProductResponseElement extends FullAuditedResponse {

    @NotBlank
    private long id;

    @NotBlank
    private int quantity;

    public ProductTransactionInProductResponseElement() { }

    public long getId() {
        return id;
    }

    public ProductTransactionInProductResponseElement setId(long id) {
        this.id = id;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductTransactionInProductResponseElement setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }
}
