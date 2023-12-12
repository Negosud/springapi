package fr.negosud.springapi.api.controller;

import fr.negosud.springapi.api.model.entity.ProductFamily;
import fr.negosud.springapi.api.service.ProductFamilyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productFamily")
@Tag(name = "ProductFamily")
public class ProductFamilyController {

    private final ProductFamilyService productFamilyService;

    @Autowired
    public ProductFamilyController(ProductFamilyService productFamilyService) {

        this.productFamilyService = productFamilyService;
    }

    @GetMapping
    public ResponseEntity<List<ProductFamily>> getAllProductFamilies() {

        List<ProductFamily> productFamilies = productFamilyService.getAllProductFamilies();
        return new ResponseEntity<>(productFamilies, HttpStatus.OK);
    }

    @GetMapping("/{productFamilyId}")
    public ResponseEntity<ProductFamily> getProductFamilyById(@PathVariable Long productFamilyId) {

        return productFamilyService.getProductFamilyById(productFamilyId)
                .map(productFamily -> new ResponseEntity<>(productFamily, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<ProductFamily> createProductFamily(@RequestBody ProductFamily productFamily) {

        ProductFamily createdProductFamily= productFamilyService.saveProductFamily(productFamily);
        return new ResponseEntity<>(createdProductFamily, HttpStatus.CREATED);
    }

    @PutMapping("/{productFamilyId}")
    public ResponseEntity<ProductFamily> updateProductFamily(@PathVariable Long productFamilyId, @RequestBody ProductFamily productFamily) {

        if (productFamilyService.getProductFamilyById(productFamilyId).isPresent()) {
            productFamily.setProductFamilyId(productFamilyId);
            ProductFamily updatedProductFamily = productFamilyService.saveProductFamily(productFamily);
            return new ResponseEntity<>(updatedProductFamily, HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{productFamilyId}")
    public ResponseEntity<Void> deleteProductFamily(@PathVariable Long productFamilyId) {

        if (productFamilyService.getProductFamilyById(productFamilyId).isPresent()) {
            productFamilyService.deleteProductFamily(productFamilyId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}