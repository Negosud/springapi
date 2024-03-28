package fr.negosud.springapi.api.model.dto;

import jdk.jfr.Description;

import java.util.List;

public class PlaceOrderRequest {

    @Description("A list of product ids and quantities")
    private List<SetOrderedProductElement> orderedProducts;

    public PlaceOrderRequest() { }

    public List<SetOrderedProductElement> getOrderedProducts() {
        return orderedProducts;
    }

    public void setOrderedProducts(List<SetOrderedProductElement> orderedProducts) {
        this.orderedProducts = orderedProducts;
    }
}
