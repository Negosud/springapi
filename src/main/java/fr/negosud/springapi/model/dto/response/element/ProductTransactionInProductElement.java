package fr.negosud.springapi.model.dto.response.element;

import fr.negosud.springapi.model.dto.response.audit.FullAuditedResponse;
import jakarta.validation.constraints.NotBlank;

public class ProductTransactionInProductElement extends FullAuditedResponse {

    @NotBlank
    private long id;

    @NotBlank
    private int quantity;

    public ProductTransactionInProductElement() { }

    public long getId() {
        return id;
    }

    public ProductTransactionInProductElement setId(long id) {
        this.id = id;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductTransactionInProductElement setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }
}
