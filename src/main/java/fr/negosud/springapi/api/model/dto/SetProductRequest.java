package fr.negosud.springapi.api.model.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public class SetProductRequest {

    @NotBlank
    @Column(length = 100)
    private String name;

    @NotBlank
    @Column(length = 1000)
    private String description;

    @NotBlank
    private int vintage;

    @NotBlank
    private String productFamilyCode;

    @NotBlank
    @Column(precision = 10, scale = 2)
    private BigDecimal unitPrice;

    private Integer quantity;

    private Boolean active;

    public SetProductRequest() { this.active = true ; }

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

    public int getVintage() {
        return vintage;
    }

    public void setVintage(int vintage) {
        this.vintage = vintage;
    }

    public String getProductFamilyCode() {
        return productFamilyCode;
    }

    public void setProductFamilyCode(String productFamilyCode) {
        this.productFamilyCode = productFamilyCode;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
