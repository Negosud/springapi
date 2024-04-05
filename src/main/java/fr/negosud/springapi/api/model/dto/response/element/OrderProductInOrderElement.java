package fr.negosud.springapi.api.model.dto.response.element;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import fr.negosud.springapi.api.model.entity.Product;
import fr.negosud.springapi.api.model.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public class OrderProductInOrderElement {

    @NotNull
    private long id;

    @NotBlank
    private int quantity;

    @Temporal(TemporalType.TIMESTAMP)
    private Date preparedAt;

    @JsonIdentityReference(alwaysAsId = true)
    private User preparedBy;

    @NotBlank
    @JsonIdentityReference(alwaysAsId = true)
    private Product product;

    public OrderProductInOrderElement() { }

    public long getId() {
        return id;
    }

    public OrderProductInOrderElement setId(long id) {
        this.id = id;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public OrderProductInOrderElement setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public Date getPreparedAt() {
        return preparedAt;
    }

    public OrderProductInOrderElement setPreparedAt(Date preparedAt) {
        this.preparedAt = preparedAt;
        return this;
    }

    public User getPreparedBy() {
        return preparedBy;
    }

    public OrderProductInOrderElement setPreparedBy(User preparedBy) {
        this.preparedBy = preparedBy;
        return this;
    }

    public Product getProduct() {
        return product;
    }

    public OrderProductInOrderElement setProduct(Product product) {
        this.product = product;
        return this;
    }
}
