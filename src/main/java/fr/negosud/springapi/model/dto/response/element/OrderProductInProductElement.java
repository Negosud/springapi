package fr.negosud.springapi.model.dto.response.element;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import fr.negosud.springapi.model.entity.Order;
import fr.negosud.springapi.model.entity.User;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public class OrderProductInProductElement {

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

    private ProductTransactionInProductElement productTransaction;

    public OrderProductInProductElement() { }

    public long getId() {
        return id;
    }

    public OrderProductInProductElement setId(long id) {
        this.id = id;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public OrderProductInProductElement setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public Date getPreparedAt() {
        return preparedAt;
    }

    public OrderProductInProductElement setPreparedAt(Date preparedAt) {
        this.preparedAt = preparedAt;
        return this;
    }

    public User getPreparedBy() {
        return preparedBy;
    }

    public OrderProductInProductElement setPreparedBy(User preparedBy) {
        this.preparedBy = preparedBy;
        return this;
    }

    public Order getOrder() {
        return order;
    }

    public OrderProductInProductElement setOrder(Order order) {
        this.order = order;
        return this;
    }

    public ProductTransactionInProductElement getProductTransaction() {
        return productTransaction;
    }

    public OrderProductInProductElement setProductTransaction(ProductTransactionInProductElement productTransaction) {
        this.productTransaction = productTransaction;
        return this;
    }
}
