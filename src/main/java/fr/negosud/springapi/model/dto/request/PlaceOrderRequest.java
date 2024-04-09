package fr.negosud.springapi.model.dto.request;

import fr.negosud.springapi.model.dto.request.element.SetOrderProductRequestElement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class PlaceOrderRequest {

    @NotNull
    @Valid
    private List<SetOrderProductRequestElement> orderProducts;

    public PlaceOrderRequest() { }

    public List<SetOrderProductRequestElement> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(List<SetOrderProductRequestElement> orderProducts) {
        this.orderProducts = orderProducts;
    }
}
