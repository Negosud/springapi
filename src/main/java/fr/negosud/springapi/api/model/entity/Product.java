package fr.negosud.springapi.api.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Year;
import java.util.List;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private long id;

    @NotBlank
    @Column(length = 100)
    private String name;

    @NotBlank
    @Column(length = 1000)
    private String description;

    @NotBlank
    private int quantity;

    @NotBlank
    private Year vintage;

    @ManyToOne
    private ProductFamily productFamily;

    @NotBlank
    @Column(precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @NotBlank
    @Column(precision = 10, scale = 2)
    private BigDecimal unitPriceVAT;

    @NotBlank
    private boolean active;

    @OneToMany(mappedBy = "product")
    private List<SupplierProduct> supplierList;

    @OneToMany(mappedBy = "product")
    private List<ArrivalProduct> arrivalList;

    @OneToMany(mappedBy = "product")
    private List<OrderProduct> orderList;

    public Product() { }

    public Product(long id, String name, String description, int quantity, Year vintage, ProductFamily productFamily, BigDecimal unitPrice, BigDecimal unitPriceVAT, boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.vintage = vintage;
        this.productFamily = productFamily;
        this.unitPrice = unitPrice;
        this.unitPriceVAT = unitPriceVAT;
        this.active = active;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Year getVintage() {
        return vintage;
    }

    public void setVintage(Year vintage) {
        this.vintage = vintage;
    }

    public ProductFamily getProductFamily() {
        return productFamily;
    }

    public void setProductFamily(ProductFamily productFamily) {
        this.productFamily = productFamily;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getUnitPriceVAT() {
        return unitPriceVAT;
    }

    public void setUnitPriceVAT(BigDecimal unitPriceVAT) {
        this.unitPriceVAT = unitPriceVAT;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
