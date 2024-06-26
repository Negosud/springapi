package fr.negosud.springapi.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import fr.negosud.springapi.model.dto.response.element.ArrivalProductInProductResponseElement;
import fr.negosud.springapi.model.dto.response.element.OrderProductInProductResponseElement;
import fr.negosud.springapi.model.dto.response.element.SupplierProductInProductResponseElement;
import fr.negosud.springapi.model.entity.Product;
import fr.negosud.springapi.model.entity.ProductFamily;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.Year;
import java.util.Date;
import java.util.List;

public class ProductResponse {

    @NotNull
    private long id;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 1000)
    private String description;

    @NotBlank
    private int quantity;

    @NotBlank
    private Date expirationDate;

    private Year vintage;

    @NotBlank
    @JsonIdentityReference(alwaysAsId = true)
    private ProductFamily productFamily;

    @NotBlank
    private BigDecimal unitPrice;

    @NotBlank
    private BigDecimal unitPriceVAT;

    @NotBlank
    private boolean active;

    @JsonIdentityReference(alwaysAsId = true)
    private Product oldProduct;

    @JsonIdentityReference(alwaysAsId = true)
    private Product newProduct;

    private List<SupplierProductInProductResponseElement> supplierList;

    private List<ArrivalProductInProductResponseElement> arrivalList;

    private List<OrderProductInProductResponseElement> orderList;

    public ProductResponse() { }

    public long getId() {
        return id;
    }

    public ProductResponse setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProductResponse setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProductResponse setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductResponse setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public ProductResponse setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public Year getVintage() {
        return vintage;
    }

    public ProductResponse setVintage(Year vintage) {
        this.vintage = vintage;
        return this;
    }

    public ProductFamily getProductFamily() {
        return productFamily;
    }

    public ProductResponse setProductFamily(ProductFamily productFamily) {
        this.productFamily = productFamily;
        return this;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public ProductResponse setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public BigDecimal getUnitPriceVAT() {
        return unitPriceVAT;
    }

    public ProductResponse setUnitPriceVAT(BigDecimal unitPriceVAT) {
        this.unitPriceVAT = unitPriceVAT;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public ProductResponse setActive(boolean active) {
        this.active = active;
        return this;
    }

    public Product getOldProduct() {
        return oldProduct;
    }

    public ProductResponse setOldProduct(Product oldProduct) {
        this.oldProduct = oldProduct;
        return this;
    }

    public Product getNewProduct() {
        return newProduct;
    }

    public ProductResponse setNewProduct(Product newProduct) {
        this.newProduct = newProduct;
        return this;
    }

    public List<SupplierProductInProductResponseElement> getSupplierList() {
        return supplierList;
    }

    public ProductResponse setSupplierList(List<SupplierProductInProductResponseElement> supplierList) {
        this.supplierList = supplierList;
        return this;
    }

    public List<ArrivalProductInProductResponseElement> getArrivalList() {
        return arrivalList;
    }

    public ProductResponse setArrivalList(List<ArrivalProductInProductResponseElement> arrivalList) {
        this.arrivalList = arrivalList;
        return this;
    }

    public List<OrderProductInProductResponseElement> getOrderList() {
        return orderList;
    }

    public ProductResponse setOrderList(List<OrderProductInProductResponseElement> orderList) {
        this.orderList = orderList;
        return this;
    }
}
