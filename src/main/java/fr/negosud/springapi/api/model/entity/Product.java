package fr.negosud.springapi.api.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import fr.negosud.springapi.api.entity.ProductFamily;


import java.math.BigDecimal;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private Long productId;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String description;

    @NotBlank
    @Column(nullable = false)
    private Integer quantity;

    @NotBlank
    @Column(nullable = false)
    private Integer vintage;

    @OneToOne(mappedBy = "ProductFamily", cascade = CascadeType.DETACH )
    private ProductFamily productFamily;

    @NotBlank
    @Column(nullable = false)
    private String unitPrice;

    @NotBlank
    @Column(nullable = false)
    private String unitPriceVAT;

    @NotBlank
    @Column(nullable = false)
    private Boolean active;

    public Product() {
    }

    public Product(Long productId, String name, String description, Integer quantity, Integer vintage, ProductFamily productFamily, String unitPrice, String unitPriceVAT, Boolean active) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.vintage = vintage;
        this.productFamily = productFamily;
        this.unitPrice = unitPrice;
        this.unitPriceVAT = unitPriceVAT;
        this.active = active;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getVintage() {
        return vintage;
    }

    public void setVintage(Integer vintage) {
        this.vintage = vintage;
    }

    public ProductFamily getProductFamily() {
        return productFamily;
    }

    public void setProductFamily(ProductFamily productFamily) {
        this.productFamily = productFamily;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getUnitPriceVAT() {
        return unitPriceVAT;
    }

    public void setUnitPriceVAT(String unitPriceVAT) {
        this.unitPriceVAT = unitPriceVAT;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
