package fr.negosud.springapi.model.dto.response.element;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import fr.negosud.springapi.model.entity.Product;
import jakarta.validation.constraints.NotBlank;

public class ArrivalProductInArrivalElement {

    @NotBlank
    private long id;

    @NotBlank
    private int quantity;

    @NotBlank
    @JsonIdentityReference(alwaysAsId = true)
    private Product product;

    private ProductTransactionInProductElement productTransaction;

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

    public ProductTransactionInProductElement getProductTransaction() {
        return productTransaction;
    }

    public ArrivalProductInArrivalElement setProductTransaction(ProductTransactionInProductElement productTransaction) {
        this.productTransaction = productTransaction;
        return this;
    }
}
