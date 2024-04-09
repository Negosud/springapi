package fr.negosud.springapi.model.entity;

import com.fasterxml.jackson.annotation.*;
import fr.negosud.springapi.model.entity.listener.AuditListener;
import fr.negosud.springapi.model.entity.audit.FullAuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Date;
import java.time.Year;
import java.util.List;

@Entity
@EntityListeners(AuditListener.class)
@Table(name="\"product\"")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Product extends FullAuditableEntity {

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
    private Date expirationDate;

    private Year vintage;

    @ManyToOne
    @NotBlank
    private ProductFamily productFamily;

    @NotBlank
    @Column(precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @NotBlank
    @Column(precision = 10, scale = 2)
    private BigDecimal unitPriceVAT;

    @NotBlank
    private boolean active;

    @OneToOne
    private Product oldProduct;

    @OneToOne(mappedBy = "oldProduct")
    private Product newProduct;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SupplierProduct> supplierList;

    @OneToMany(mappedBy = "product")
    private List<ArrivalProduct> arrivalList;

    @OneToMany(mappedBy = "product")
    private List<OrderProduct> orderList;

    public Product() {
        this.active = true;
        this.quantity = 0;
    }

    public Product (String name, String description, int quantity, Date expirationDate, Year vintage, ProductFamily productFamily, BigDecimal unitPrice, boolean active) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.expirationDate = expirationDate;
        this.vintage = vintage;
        this.productFamily = productFamily;
        this.unitPrice = unitPrice;
        this.unitPriceVAT = unitPrice.multiply(new BigDecimal("1.20"));
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

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Product getOldProduct() {
        return oldProduct;
    }

    public void setOldProduct(Product oldProduct) {
        this.oldProduct = oldProduct;
    }

    public Product getNewProduct() {
        return newProduct;
    }

    public void setNewProduct(Product newProduct) {
        this.newProduct = newProduct;
    }

    public List<SupplierProduct> getSupplierList() {
        return supplierList;
    }

    public void setSupplierList(List<SupplierProduct> supplierList) {
        this.supplierList = supplierList;
    }

    public List<ArrivalProduct> getArrivalList() {
        return arrivalList;
    }

    public void setArrivalList(List<ArrivalProduct> arrivalList) {
        this.arrivalList = arrivalList;
    }

    public List<OrderProduct> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderProduct> orderList) {
        this.orderList = orderList;
    }
}