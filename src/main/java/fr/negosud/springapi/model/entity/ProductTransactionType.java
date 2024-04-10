package fr.negosud.springapi.model.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import fr.negosud.springapi.model.entity.listener.AuditListener;
import fr.negosud.springapi.model.entity.audit.FullAuditableEntity;
import fr.negosud.springapi.util.Strings;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@Entity
@EntityListeners(AuditListener.class)
@Table(name="\"product_transaction_type\"")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "code")
public class ProductTransactionType extends FullAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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
    @OneToMany(mappedBy = "produtTransactionType")
    List<ProductTransaction> productTransactionList;

    public ProductTransactionType() {
        this.isRemovable = true;
    }

    /**
     * Constructor used by ProductTransactionType init method
     */
    public ProductTransactionType(String name, String description, boolean isEntry) {
        this.name = name;
        this.description = description;
        this.isEntry = isEntry;
        this.code = Strings.getCodeFromName(name);
        this.isRemovable = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public List<ProductTransaction> getProductTransactionList() {
        return productTransactionList;
    }

    public void setProductTransactionList(List<ProductTransaction> productTransactionList) {
        this.productTransactionList = productTransactionList;
    }
}
