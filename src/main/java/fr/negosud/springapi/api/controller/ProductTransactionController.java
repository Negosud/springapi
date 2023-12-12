package fr.negosud.springapi.api.controller;

import fr.negosud.springapi.api.model.entity.ProductTransaction;
import fr.negosud.springapi.api.service.ProductTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public class ProductTransactionController {

    private ProductTransactionService productTransactionService;

    @Autowired
    public ProductTransactionController(ProductTransactionService productTransactionService) {
        this.productTransactionService =  productTransactionService;
    }

    @GetMapping
    public ResponseEntity<List<ProductTransaction>> getAllProductTransactionType() {
        List<ProductTransaction> productTransactions = productTransactionService.getAllProductTransaction();
        return new ResponseEntity<>(productTransactions, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ProductTransaction> getProductTransactionById(@PathVariable Long productTransactionId) {
        return productTransactionService.getProductTransactionById(productTransactionId)
                .map(product -> new ResponseEntity<>(product, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{productTransactionTypeId}")
    public ResponseEntity<ProductTransaction> updateTransaction(@PathVariable Long productTransactionId, @RequestBody ProductTransaction productTransaction) {
        if(productTransactionService.getProductTransactionById(productTransactionId).isPresent()) {
            productTransaction.setProductTransactionId(productTransactionId);
            ProductTransaction updatedProductTransaction = productTransactionService.saveProductTransaction(productTransaction);
            return new ResponseEntity<>(updatedProductTransaction, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
