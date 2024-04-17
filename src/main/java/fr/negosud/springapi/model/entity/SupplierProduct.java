package fr.negosud.springapi.model.entity;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import fr.negosud.springapi.model.entity.composite.SupplierProductKey;
import fr.negosud.springapi.model.entity.listener.AuditListener;
import fr.negosud.springapi.model.entity.audit.FullAuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

@Entity
@EntityListeners(AuditListener.class)
@Table(name="\"supplier_product\"")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class SupplierProduct extends FullAuditableEntity {

    @EmbeddedId
    private SupplierProductKey id;

    @NotBlank
    private int quantity;

    @NotBlank
    @Column(precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @ManyToOne
    @JoinColumn(insertable=false, updatable=false)
    @NotBlank
    @JsonIdentityReference(alwaysAsId = true)
    private User supplier;

    @ManyToOne
    @JoinColumn(insertable=false, updatable=false)
    @NotBlank
    @JsonIdentityReference(alwaysAsId = true)
    private Product product;

    public SupplierProduct() { }

    public SupplierProductKey getId() {
        return id;
    }

    public void setId(SupplierProductKey id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public User getSupplier() {
        return supplier;
    }

    public void setSupplier(User supplier) {
        this.supplier = supplier;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
