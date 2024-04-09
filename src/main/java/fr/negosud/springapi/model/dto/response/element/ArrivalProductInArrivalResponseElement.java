package fr.negosud.springapi.model.dto.response.element;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import fr.negosud.springapi.model.entity.Product;
import jakarta.validation.constraints.NotBlank;

public class ArrivalProductInArrivalResponseElement {

    @NotBlank
    private long id;

    @NotBlank
    private int quantity;

    @NotBlank
    @JsonIdentityReference(alwaysAsId = true)
    private Product product;

    private ProductTransactionInProductResponseElement productTransaction;

    public ArrivalProductInArrivalResponseElement() { }

    public long getId() {
        return id;
    }

    public ArrivalProductInArrivalResponseElement setId(long id) {
        this.id = id;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public ArrivalProductInArrivalResponseElement setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public Product getProduct() {
        return product;
    }

    public ArrivalProductInArrivalResponseElement setProduct(Product product) {
        this.product = product;
        return this;
    }

    public ProductTransactionInProductResponseElement getProductTransaction() {
        return productTransaction;
    }

    public ArrivalProductInArrivalResponseElement setProductTransaction(ProductTransactionInProductResponseElement productTransaction) {
        this.productTransaction = productTransaction;
        return this;
    }
}
