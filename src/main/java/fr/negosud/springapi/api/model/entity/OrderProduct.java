package fr.negosud.springapi.api.model.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import fr.negosud.springapi.api.model.entity.listener.OrderProductListener;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

@Entity
@EntityListeners(OrderProductListener.class)
@Table(name="\"order_product\"")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    private int quantity;

    @Temporal(TemporalType.TIMESTAMP)
    private Date preparedAt;

    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    private User preparedBy;

    @ManyToOne
    @NotBlank
    @JsonIdentityReference(alwaysAsId = true)
    private Order order;

    @ManyToOne
    @NotBlank
    @JsonIdentityReference(alwaysAsId = true)
    private Product product;

    public OrderProduct() { }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getPreparedAt() {
        return preparedAt;
    }

    public void setPreparedAt(Date preparedAt) {
        this.preparedAt = preparedAt;
    }

    public User getPreparedBy() {
        return preparedBy;
    }

    public void setPreparedBy(User preparedBy) {
        this.preparedBy = preparedBy;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
