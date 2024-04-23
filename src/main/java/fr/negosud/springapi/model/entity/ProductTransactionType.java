package fr.negosud.springapi.model.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import fr.negosud.springapi.model.entity.listener.AuditListener;
import fr.negosud.springapi.model.entity.audit.FullAuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@EntityListeners(AuditListener.class)
@Table(name="\"product_transaction_type\"")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "code")
public class ProductTransactionType extends FullAuditableEntity {

    @Id
    @NotBlank
    @Column(length = 100, unique = true)
    private String code;

    @NotBlank
    @Column(length = 100, unique = true)
    private String name;

    @Column(length = 1000)
    private String description;

    @NotBlank
    private boolean isEntry;

    @NotBlank
    @ColumnDefault(value = "true")
    private boolean isRemovable;

    @JsonIgnore
    @OneToMany(mappedBy = "produtTransactionType", fetch = FetchType.LAZY)
    List<ProductTransaction> productTransactions;

    public ProductTransactionType() {
        this.isRemovable = true;
        productTransactions = new ArrayList<>();
    }

    /**
     * Constructor used by ProductTransactionType init method
     */
    public ProductTransactionType(String name, String description, boolean isEntry) {
        this.name = name;
        this.description = description;
        this.isEntry = isEntry;
        this.isRemovable = false;
        productTransactions = new ArrayList<>();
    }

    public String toString() {
        return "ProductTransactionType [code=" + code + ", name=" + name + ", description=" + description + ", isEntry=" + isEntry + "]";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public boolean isEntry() {
        return isEntry;
    }

    public void setEntry(boolean entry) {
        isEntry = entry;
    }

    public boolean isRemovable() {
        return isRemovable;
    }

    public void setRemovable(boolean removable) {
        isRemovable = removable;
    }

    public List<ProductTransaction> getProductTransactions() {
        return productTransactions;
    }

    public void setProductTransactions(List<ProductTransaction> productTransactionList) {
        this.productTransactions = productTransactionList;
    }
}
