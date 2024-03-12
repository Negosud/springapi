package fr.negosud.springapi.api.model.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import fr.negosud.springapi.api.audit.AuditListener;
import fr.negosud.springapi.api.audit.CreationAuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@EntityListeners(AuditListener.class)
@Table(name="\"order_product\"")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class OrderProduct extends CreationAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    private int quantity;

    @ManyToOne
    @NotBlank
    private Order order;

    @ManyToOne
    @NotBlank
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
