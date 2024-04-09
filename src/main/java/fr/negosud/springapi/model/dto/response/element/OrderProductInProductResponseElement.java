package fr.negosud.springapi.model.dto.response.element;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import fr.negosud.springapi.model.entity.Order;
import fr.negosud.springapi.model.entity.User;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public class OrderProductInProductResponseElement {

    @NotBlank
    private long id;

    @NotBlank
    private int quantity;

    private Date preparedAt;

    @JsonIdentityReference(alwaysAsId = true)
    private User preparedBy;

    @NotBlank
    @JsonIdentityReference(alwaysAsId = true)
    private Order order;

    private ProductTransactionInProductResponseElement productTransaction;

    public OrderProductInProductResponseElement() { }

    public long getId() {
        return id;
    }

    public OrderProductInProductResponseElement setId(long id) {
        this.id = id;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public OrderProductInProductResponseElement setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public Date getPreparedAt() {
        return preparedAt;
    }

    public OrderProductInProductResponseElement setPreparedAt(Date preparedAt) {
        this.preparedAt = preparedAt;
        return this;
    }

    public User getPreparedBy() {
        return preparedBy;
    }

    public OrderProductInProductResponseElement setPreparedBy(User preparedBy) {
        this.preparedBy = preparedBy;
        return this;
    }

    public Order getOrder() {
        return order;
    }

    public OrderProductInProductResponseElement setOrder(Order order) {
        this.order = order;
        return this;
    }

    public ProductTransactionInProductResponseElement getProductTransaction() {
        return productTransaction;
    }

    public OrderProductInProductResponseElement setProductTransaction(ProductTransactionInProductResponseElement productTransaction) {
        this.productTransaction = productTransaction;
        return this;
    }
}
