package fr.negosud.springapi.model.dto.response.element;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import fr.negosud.springapi.model.entity.Product;
import fr.negosud.springapi.model.entity.User;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public class OrderProductInOrderResponseElement {

    @NotBlank
    private long id;

    @NotBlank
    private int quantity;

    private Date preparedAt;

    @JsonIdentityReference(alwaysAsId = true)
    private User preparedBy;

    @NotBlank
    @JsonIdentityReference(alwaysAsId = true)
    private Product product;

    private ProductTransactionInProductResponseElement productTransaction;

    public OrderProductInOrderResponseElement() { }

    public long getId() {
        return id;
    }

    public OrderProductInOrderResponseElement setId(long id) {
        this.id = id;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public OrderProductInOrderResponseElement setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public Date getPreparedAt() {
        return preparedAt;
    }

    public OrderProductInOrderResponseElement setPreparedAt(Date preparedAt) {
        this.preparedAt = preparedAt;
        return this;
    }

    public User getPreparedBy() {
        return preparedBy;
    }

    public OrderProductInOrderResponseElement setPreparedBy(User preparedBy) {
        this.preparedBy = preparedBy;
        return this;
    }

    public Product getProduct() {
        return product;
    }

    public OrderProductInOrderResponseElement setProduct(Product product) {
        this.product = product;
        return this;
    }

    public ProductTransactionInProductResponseElement getProductTransaction() {
        return productTransaction;
    }

    public OrderProductInOrderResponseElement setProductTransaction(ProductTransactionInProductResponseElement productTransaction) {
        this.productTransaction = productTransaction;
        return this;
    }
}
