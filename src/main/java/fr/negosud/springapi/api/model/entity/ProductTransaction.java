package fr.negosud.springapi.api.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class ProductTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private Long productTransactionId;
    @OneToOne
    private Product product;

    @NotBlank
    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne
    private  ProductTransactionType produtTransactionType;

    public ProductTransaction() {
    }

    public ProductTransaction(Long productTransactionId, Product product, Integer quantity, ProductTransactionType produtTransactionType) {
        this.productTransactionId = productTransactionId;
        this.product = product;
        this.quantity = quantity;
        this.produtTransactionType = produtTransactionType;
    }

    public Long getProductTransactionId() {
        return productTransactionId;
    }

    public void setProductTransactionId(Long productTransactionId) {
        this.productTransactionId = productTransactionId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public ProductTransactionType getProdutTransactionType() {
        return produtTransactionType;
    }

    public void setProdutTransactionType(ProductTransactionType produtTransactionType) {
        this.produtTransactionType = produtTransactionType;
    }
}
