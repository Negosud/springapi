package fr.negosud.springapi.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import fr.negosud.springapi.model.ArrivalStatus;
import fr.negosud.springapi.model.dto.response.audit.FullAuditedResponse;
import fr.negosud.springapi.model.dto.response.element.ArrivalProductInArrivalResponseElement;
import fr.negosud.springapi.model.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public class ArrivalResponse extends FullAuditedResponse {

    private long id;

    @NotBlank
    @Size(max = 20)
    private String reference;

    @NotBlank
    private ArrivalStatus status;

    @NotBlank
    @JsonIdentityReference(alwaysAsId = true)
    private User suppliedBy;

    @Size(max = 1000)
    private String comment;

    private List<ArrivalProductInArrivalResponseElement> productList;

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

    public List<ArrivalProductInArrivalResponseElement> getProductList() {
        return productList;
    }

    public ArrivalResponse setProductList(List<ArrivalProductInArrivalResponseElement> productList) {
        this.productList = productList;
        return this;
    }
}
