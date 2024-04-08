package fr.negosud.springapi.api.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import fr.negosud.springapi.api.model.OrderStatus;
import fr.negosud.springapi.api.model.dto.response.element.OrderProductInOrderElement;
import fr.negosud.springapi.api.model.entity.Invoice;
import fr.negosud.springapi.api.model.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

public class OrderResponse {

    @NotNull
    private long id;

    @NotBlank
    @Column(length = 20)
    private String reference;

    @NotBlank
    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;

    @JsonIdentityReference(alwaysAsId = true)
    private User preparedBy;

    private Date preparedAt;

    @OneToOne(mappedBy = "order")
    @JsonIdentityReference(alwaysAsId = true)
    private Invoice invoice;

    private List<OrderProductInOrderElement> productList;

    public OrderResponse() { }

    public long getId() {
        return id;
    }

    public OrderResponse setId(long id) {
        this.id = id;
        return this;
    }

    public String getReference() {
        return reference;
    }

    public OrderResponse setReference(String reference) {
        this.reference = reference;
        return this;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public OrderResponse setStatus(OrderStatus status) {
        this.status = status;
        return this;
    }

    public User getPreparedBy() {
        return preparedBy;
    }

    public OrderResponse setPreparedBy(User preparedBy) {
        this.preparedBy = preparedBy;
        return this;
    }

    public Date getPreparedAt() {
        return preparedAt;
    }

    public OrderResponse setPreparedAt(Date preparedAt) {
        this.preparedAt = preparedAt;
        return this;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public OrderResponse setInvoice(Invoice invoice) {
        this.invoice = invoice;
        return this;
    }

    public List<OrderProductInOrderElement> getProductList() {
        return productList;
    }

    public OrderResponse setProductList(List<OrderProductInOrderElement> productList) {
        this.productList = productList;
        return this;
    }
}
