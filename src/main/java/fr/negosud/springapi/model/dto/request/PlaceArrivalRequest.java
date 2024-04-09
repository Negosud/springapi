package fr.negosud.springapi.model.dto.request;

import fr.negosud.springapi.model.dto.request.element.SetArrivalProductRequestElement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class PlaceArrivalRequest {

    @NotNull
    @Valid
    private List<SetArrivalProductRequestElement> arrivalProducts;

    @NotBlank
    private Long supplierId;

    @Size(max = 1000)
    private String comment;

    public PlaceArrivalRequest() { }

    public List<SetArrivalProductRequestElement> getArrivalProducts() {
        return arrivalProducts;
    }

    public void setArrivalProducts(List<SetArrivalProductRequestElement> arrivalProducts) {
        this.arrivalProducts = arrivalProducts;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
