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

    public SupplierProduct setId(SupplierProductKey id) {
        this.id = id;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public SupplierProduct setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public SupplierProduct setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public User getSupplier() {
        return supplier;
    }

    public SupplierProduct setSupplier(User supplier) {
        this.supplier = supplier;
        return this;
    }

    public Product getProduct() {
        return product;
    }

    public SupplierProduct setProduct(Product product) {
        this.product = product;
        return this;
    }
}
