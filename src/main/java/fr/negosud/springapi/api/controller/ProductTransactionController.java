package fr.negosud.springapi.api.controller;

import fr.negosud.springapi.api.model.entity.ProductTransaction;
import fr.negosud.springapi.api.service.ProductTransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product_transaction")
@Tag(name = "ProductTransaction")
public class ProductTransactionController {

    final private ProductTransactionService productTransactionService;

    @Autowired
    public ProductTransactionController(ProductTransactionService productTransactionService) {
        this.productTransactionService =  productTransactionService;
    }

    @GetMapping
    public ResponseEntity<List<ProductTransaction>> getAllProductTransactionType() {
        List<ProductTransaction> productTransactions = productTransactionService.getAllProductTransaction();
        return new ResponseEntity<>(productTransactions, HttpStatus.OK);
    }

    @GetMapping("/{productTransactionId}")
    public ResponseEntity<ProductTransaction> getProductTransactionById(@PathVariable long productTransactionId) {
        return productTransactionService.getProductTransactionById(productTransactionId)
                .map(product -> new ResponseEntity<>(product, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{productTransactionId}")
    public ResponseEntity<ProductTransaction> updateTransaction(@PathVariable long productTransactionId, @RequestBody ProductTransaction productTransaction) {
        if(productTransactionService.getProductTransactionById(productTransactionId).isPresent()) {
            productTransaction.setId(productTransactionId);
            ProductTransaction updatedProductTransaction = productTransactionService.saveProductTransaction(productTransaction);
            return new ResponseEntity<>(updatedProductTransaction, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
// TODO: Définir quels sont les besoins de crud manuel sur l'entité
