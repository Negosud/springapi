package fr.negosud.springapi.api.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class ProductTransactionType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private Long productTransactionTypeId;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String description;

    @NotBlank
    @Column(nullable = false)
    private Boolean isEntry;

    public ProductTransactionType() {
    }

    public ProductTransactionType(Long productTransactionTypeId, String name, String description, Boolean isEntry) {
        this.productTransactionTypeId = productTransactionTypeId;
        this.name = name;
        this.description = description;
        this.isEntry = isEntry;
    }

    public Long getProductTransactionTypeId() {
        return productTransactionTypeId;
    }

    public void setProductTransactionTypeId(Long productTransactionTypeId) {
        this.productTransactionTypeId = productTransactionTypeId;
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

    public Boolean getEntry() {
        return isEntry;
    }

    public void setEntry(Boolean entry) {
        isEntry = entry;
    }
}
