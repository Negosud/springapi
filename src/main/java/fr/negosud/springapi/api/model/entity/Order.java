package fr.negosud.springapi.api.model.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import fr.negosud.springapi.api.audit.AuditListener;
import fr.negosud.springapi.api.audit.FullAuditableEntity;
import fr.negosud.springapi.api.model.annotation.AutoReference;
import fr.negosud.springapi.api.model.constraint.ReferencedEntityConstraint;
import fr.negosud.springapi.api.model.dto.OrderStatus;
import fr.negosud.springapi.api.model.listener.ReferenceListener;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;
import java.util.List;

@Entity
@EntityListeners({AuditListener.class, ReferenceListener.class})
@Table(name="\"order\"")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "reference")
@AutoReference(referenceCode = "ORDR")
public class Order extends FullAuditableEntity implements ReferencedEntityConstraint {

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
    @JsonIdentityReference(alwaysAsId = true)
    private User preparedBy;

    private Date preparedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> productList;

    public Order() { }

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

    public List<OrderProduct> getProductList() {
        return productList;
    }

    public void setProductList(List<OrderProduct> productList) {
        this.productList = productList;
    }
}
