package fr.negosud.springapi.model.entity.composite;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class SupplierProductKey implements Serializable {
    private Long supplierId;
    private Long productId;

    public SupplierProductKey() { }

    public SupplierProductKey(Long supplierId, Long productId) {
        this.supplierId = supplierId;
        this.productId = productId;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SupplierProductKey && ((SupplierProductKey) obj).getSupplierId().equals(getSupplierId()) && ((SupplierProductKey) obj).getProductId().equals(getProductId());
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
