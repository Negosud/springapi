package fr.negosud.springapi.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private Long shoppingCartId;

    @NotBlank
    @Column(nullable = false)
    private BigDecimal totalPriceVAT;

    @NotBlank
    @Column(nullable = false)
    private BigDecimal totalPriceHT;

    @NotBlank
    @Column(nullable = false)
    private Long userId;

    public ShoppingCart() { }

    public ShoppingCart(Long shoppingCartId, BigDecimal totalPriceVAT, BigDecimal totalPriceHT, Long userId) {

        this.shoppingCartId = shoppingCartId;
        this.totalPriceVAT = totalPriceVAT;
        this.totalPriceHT = totalPriceHT;
        this.userId = userId;
    }

    public Long getShoppingCartId() {

        return shoppingCartId;
    }

    public void setShoppingCartId(Long shoppingCartId) {

        this.shoppingCartId = shoppingCartId;
    }

    public BigDecimal getTotalPriceVAT() {

        return totalPriceVAT;
    }

    public void setTotalPriceVAT(BigDecimal totalPriceVAT) {

        this.totalPriceVAT = totalPriceVAT;
    }

    public BigDecimal getTotalPriceHT() {

        return totalPriceHT;
    }

    public void setTotalPriceHT(BigDecimal totalPriceHT) {

        this.totalPriceHT = totalPriceHT;
    }

    public Long getUserId() {

        return userId;
    }

    public void setUserId(Long userId) {

        this.userId = userId;
    }
}
