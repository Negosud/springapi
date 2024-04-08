package fr.negosud.springapi.api.model.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import fr.negosud.springapi.api.model.entity.listener.AuditListener;
import fr.negosud.springapi.api.model.entity.audit.FullAuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@EntityListeners(AuditListener.class)
@Table(name = "\"product_transaction\"")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ProductTransaction extends FullAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @NotBlank
    @JsonIdentityReference(alwaysAsId = true)
    private Product product;

    @NotBlank
    private int quantity;

    @ManyToOne
    @NotBlank
    @JsonIdentityReference(alwaysAsId = true)
    private ProductTransactionType produtTransactionType;

    public ProductTransaction() { }

    /**
     * Constructor used by handleProductQuantityDefinition method
     */
    public ProductTransaction(Product product, int quantity, ProductTransactionType productTransactionType) {
        this.product = product;
        this.quantity = quantity;
        this.produtTransactionType = productTransactionType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ProductTransactionType getProdutTransactionType() {
        return produtTransactionType;
    }

    public void setProdutTransactionType(ProductTransactionType produtTransactionType) {
        this.produtTransactionType = produtTransactionType;
    }
}
