package fr.negosud.springapi.api.model.dto.request;

import fr.negosud.springapi.api.model.dto.request.element.SetOrderProductElement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class PlaceOrderRequest {

    @NotNull
    @Valid
    private List<SetOrderProductElement> orderProducts;

    public PlaceOrderRequest() { }

    public List<SetOrderProductElement> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(List<SetOrderProductElement> orderProducts) {
        this.orderProducts = orderProducts;
    }
}
