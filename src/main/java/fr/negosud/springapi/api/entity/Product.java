package fr.negosud.springapi.api.entity;

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
    private String description

    @NotBlank
    @Column(nullable = false)
    private Integer quantity

    @NotBlank
    @Column(nullable = false)
    private Integer vintage

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
    private Bool active;

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

    public String getProductFamily() {
        return productFamily;
    }

    public void setProductFamily(String productFamily) {
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

    public Bool getActive() {
        return active;
    }

    public void setActive(Bool active) {
        this.active = active;
    }
}
