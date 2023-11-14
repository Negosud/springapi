package fr.negosud.springapi.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class ProductFamily {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private Long productFamilyId;

    @NotBlank
    @Column(nullable = false)
    private String name;

    public ProductFamily() {
    }

    public ProductFamily(Long productFamilyId, String name) {

        this.productFamilyId = productFamilyId;
        this.name = name;
    }

    public Long getProductFamilyId() {

        return productFamilyId;
    }

    public void setProductFamilyId(Long productFamilyId) {

        this.productFamilyId = productFamilyId;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }
}
