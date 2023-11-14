package fr.negosud.springapi.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private Long productId;

    @NotBlank
    @Column(nullable = false)
    private BigDecimal priceVAT;

    @NotBlank
    @Column(nullable = false)
    private BigDecimal priceHT;

    @NotBlank
    @Column(nullable = false)
    private Integer quantity;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private Integer family;

    public Product() { }

    public Product(Long productId, BigDecimal priceVAT, BigDecimal priceHT, Integer quantity, String name, Integer family) {

        this.productId = productId;
        this.priceVAT = priceVAT;
        this.priceHT = priceHT;
        this.quantity = quantity;
        this.name = name;
        this.family = family;
    }

    public Long getProductId() {

        return productId;
    }

    public void setProductId(Long productId) {

        this.productId = productId;
    }

    public BigDecimal getPriceVAT() {

        return priceVAT;
    }

    public void setPriceVAT(BigDecimal priceVAT) {

        this.priceVAT = priceVAT;
    }

    public BigDecimal getPriceHT() {

        return priceHT;
    }

    public void setPriceHT(BigDecimal priceHT) {

        this.priceHT = priceHT;
    }

    public Integer getQuantity() {

        return quantity;
    }

    public void setQuantity(Integer quantity) {

        this.quantity = quantity;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public Integer getFamily() {

        return family;
    }

    public void setFamily(Integer family) {

        this.family = family;
    }
}
