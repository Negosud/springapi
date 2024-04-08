package fr.negosud.springapi.api.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import fr.negosud.springapi.api.model.ArrivalStatus;
import fr.negosud.springapi.api.model.dto.response.element.ArrivalProductInArrivalElement;
import fr.negosud.springapi.api.model.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class ArrivalResponse {

    private long id;

    @NotBlank
    @Column(length = 20)
    private String reference;

    @NotBlank
    @Enumerated(value = EnumType.STRING)
    private ArrivalStatus status;

    @NotBlank
    @JsonIdentityReference(alwaysAsId = true)
    private User suppliedBy;

    @Column(length = 1000)
    private String comment;

    private List<ArrivalProductInArrivalElement> productList;

    public ArrivalResponse() { }

    public long getId() {
        return id;
    }

    public ArrivalResponse setId(long id) {
        this.id = id;
        return this;
    }

    public String getReference() {
        return reference;
    }

    public ArrivalResponse setReference(String reference) {
        this.reference = reference;
        return this;
    }

    public ArrivalStatus getStatus() {
        return status;
    }

    public ArrivalResponse setStatus(ArrivalStatus status) {
        this.status = status;
        return this;
    }

    public User getSuppliedBy() {
        return suppliedBy;
    }

    public ArrivalResponse setSuppliedBy(User suppliedBy) {
        this.suppliedBy = suppliedBy;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public ArrivalResponse setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public List<ArrivalProductInArrivalElement> getProductList() {
        return productList;
    }

    public ArrivalResponse setProductList(List<ArrivalProductInArrivalElement> productList) {
        this.productList = productList;
        return this;
    }
}
