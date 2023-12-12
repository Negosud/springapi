package fr.negosud.springapi.api.controller;

import fr.negosud.springapi.api.model.entity.ProductTransactionType;
import fr.negosud.springapi.api.service.ProductTransactionTypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product_transaction_type")
@Tag(name = "ProductTransactionType")
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

    @GetMapping("/{productTransactionTypeId}")
    public ResponseEntity<ProductTransactionType> getProductTransactionTypeById(@PathVariable Long productTransactionTypeId) {
        return productTransactionTypeService.getProductTransactionTypeById(productTransactionTypeId)
                .map(product -> new ResponseEntity<>(product, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{productTransactionTypeId}")
    public ResponseEntity<ProductTransactionType> updateTransactionType(@PathVariable Long productTransactionTypeId, @RequestBody ProductTransactionType productTransactionType) {
        if(productTransactionTypeService.getProductTransactionTypeById(productTransactionTypeId).isPresent()) {
            productTransactionType.setProductTransactionTypeId(productTransactionTypeId);
            ProductTransactionType updatedProductTransactionType = productTransactionTypeService.saveProductTransactionType(productTransactionType);
            return new ResponseEntity<>(updatedProductTransactionType, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{productTransactionTypeId}")
    public ResponseEntity<Void>  deleteProductransaction(@PathVariable Long productTransactionTypeId) {
    if(productTransactionTypeService.getProductTransactionTypeById(productTransactionTypeId).isPresent()) {
        productTransactionTypeService.deleteProductTransactionType(productTransactionTypeId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    }
}


