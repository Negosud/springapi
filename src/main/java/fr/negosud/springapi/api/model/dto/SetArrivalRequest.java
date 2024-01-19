package fr.negosud.springapi.api.model.dto;

import fr.negosud.springapi.api.model.entity.ArrivalProduct;
import fr.negosud.springapi.api.model.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class SetArrivalRequest {
    @Schema(description = "Arrival reference")
    private String reference;


    @Enumerated(value = EnumType.STRING)
    @Schema(description = "Arrival status")
    private ArrivalStatus status;

    @Schema(description = "Arrival supplier")
    private User suppliedBy;

   @Schema(description = "Arrival's product list")
    private List<ArrivalProduct> productList;

    public SetArrivalRequest() {
        this.status = ArrivalStatus.PENDING;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public ArrivalStatus getStatus() {
        return status;
    }

    public void setStatus(ArrivalStatus status) {
        this.status = status;
    }

    public User getSuppliedBy() {
        return suppliedBy;
    }

    public void setSuppliedBy(User suppliedBy) {
        this.suppliedBy = suppliedBy;
    }

    public List<ArrivalProduct> getProductList() {
        return productList;
    }

    public void setProductList(List<ArrivalProduct> productList) {
        this.productList = productList;
    }
}
