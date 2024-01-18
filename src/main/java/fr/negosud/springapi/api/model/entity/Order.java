package fr.negosud.springapi.api.model.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import fr.negosud.springapi.api.audit.AuditListener;
import fr.negosud.springapi.api.audit.FullAuditableEntity;
import fr.negosud.springapi.api.model.dto.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;
import java.util.List;

@Entity
@EntityListeners(AuditListener.class)
@Table(name="\"order\"")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "reference")
public class Order extends FullAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Column(length = 20)
    private String reference;

    @NotBlank
    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;

    @ManyToOne
    private User preparedBy;

    private Date preparedAt;

    @OneToMany(mappedBy = "order")
    private List<OrderProduct> productList;

    public Order() { }

    public Order(long id, String reference, OrderStatus status, User preparedBy, Date preparedAt) {
        this.id = id;
        this.reference = reference;
        this.status = status;
        this.preparedBy = preparedBy;
        this.preparedAt = preparedAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public User getPreparedBy() {
        return preparedBy;
    }

    public void setPreparedBy(User preparedBy) {
        this.preparedBy = preparedBy;
    }

    public Date getPreparedAt() {
        return preparedAt;
    }

    public void setPreparedAt(Date preparedAt) {
        this.preparedAt = preparedAt;
    }
}
