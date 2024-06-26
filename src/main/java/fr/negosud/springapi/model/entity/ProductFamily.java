package fr.negosud.springapi.model.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import fr.negosud.springapi.model.entity.listener.AuditListener;
import fr.negosud.springapi.model.entity.audit.FullAuditableEntity;
import fr.negosud.springapi.util.Strings;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Entity
@EntityListeners(AuditListener.class)
@Table(name = "\"product_family\"")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "code")
public class ProductFamily extends FullAuditableEntity {

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

    @JsonIgnore
    @OneToMany(mappedBy = "productFamily")
    List<Product> products;

    public ProductFamily() { }

    /**
     * Constructor used by ProductFamily init method
     */
    public ProductFamily(String name, String description) {
        this.name = name;
        this.description = description;
        this.code = Strings.getCodeFromName(name);
    }

    public String toString() {
        return "ProductFamily [id=" + id + ", code=" + code + ", name=" + name + "]";
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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> productList) {
        this.products = productList;
    }
}
