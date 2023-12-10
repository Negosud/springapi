package fr.negosud.springapi.api.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
public class ShoppingCartProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private Long shoppingCartProductId;

    @NotBlank
    @Column(nullable = false)
    private BigDecimal unitPriceVAT;

    @NotBlank
    @Column(nullable = false)
    private BigDecimal unitPriceHT;

    @NotBlank
    @Column(nullable = false)
    private Integer quantity;

    @NotBlank
    @Column(nullable = false)
    private Long productId;

    public ShoppingCartProduct() { }

    public ShoppingCartProduct(Long shoppingCartProductId, BigDecimal unitPriceVAT, BigDecimal unitPriceHT, Integer quantity, Long productId) {

        this.shoppingCartProductId = shoppingCartProductId;
        this.unitPriceVAT = unitPriceVAT;
        this.unitPriceHT = unitPriceHT;
        this.quantity = quantity;
        this.productId = productId;
    }

    public Long getShoppingCartProductId() {

        return shoppingCartProductId;
    }

    public void setShoppingCartProductId(Long shoppingCartProductId) {

        this.shoppingCartProductId = shoppingCartProductId;
    }

    public BigDecimal getUnitPriceVAT() {

        return unitPriceVAT;
    }

    public void setUnitPriceVAT(BigDecimal unitPriceVAT) {

        this.unitPriceVAT = unitPriceVAT;
    }

    public BigDecimal getUnitPriceHT() {

        return unitPriceHT;
    }

    public void setUnitPriceHT(BigDecimal unitPriceHT) {

        this.unitPriceHT = unitPriceHT;
    }

    public Integer getQuantity() {

        return quantity;
    }

    public void setQuantity(Integer quantity) {

        this.quantity = quantity;
    }

    public Long getProductId() {

        return productId;
    }

    public void setProductId(Long productId) {

        this.productId = productId;
    }
}
