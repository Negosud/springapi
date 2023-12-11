package fr.negosud.springapi.api.controller;

import fr.negosud.springapi.api.model.entity.ProductTransactionType;
import fr.negosud.springapi.api.service.ProductTransactionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

public class ProductTransactionTypeController {

    private ProductTransactionTypeService productTransactionTypeService;

    @Autowired
    public ProductTransactionTypeController(ProductTransactionTypeService productTransactionTypeService) {
        this.productTransactionTypeService =  productTransactionTypeService;
    }

    @GetMapping
    public ResponseEntity<List<ProductTransactionType>> getAllProductTransactionType() {
        List<ProductTransactionType> productTransactionTypes = productTransactionTypeService.getAllProductTransactionType();
        return new ResponseEntity<>(productTransactionTypes, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ProductTransactionType> getProductTransactionTypeById(@PathVariable Long productTransactionTypeId) {
        return productTransactionTypeService.getProductTransactionTypeById(productTransactionTypeId)
                .map(product -> new ResponseEntity<>(product, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
