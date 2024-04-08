package fr.negosud.springapi.model.dto.request;

import fr.negosud.springapi.model.dto.request.element.SetArrivalProductElement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class PlaceArrivalRequest {

    @NotNull
    @Valid
    private List<SetArrivalProductElement> arrivalProductElements;

    @NotBlank
    private Long supplierId;

    @Size(max = 1000)
    private String comment;

    public PlaceArrivalRequest() { }

    public List<SetArrivalProductElement> getArrivalProductElements() {
        return arrivalProductElements;
    }

    public void setArrivalProductElements(List<SetArrivalProductElement> arrivalProductElements) {
        this.arrivalProductElements = arrivalProductElements;
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
