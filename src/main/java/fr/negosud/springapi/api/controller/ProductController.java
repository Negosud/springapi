package fr.negosud.springapi.api.controller;

import fr.negosud.springapi.api.component.ActionUserContextHolder;
import fr.negosud.springapi.api.model.dto.SetProductRequest;
import fr.negosud.springapi.api.model.entity.Product;
import fr.negosud.springapi.api.service.ProductService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
@Tag(name = "Product", description = "Endpoints related to Product crud and actions.")
public class ProductController {

    private final ProductService productService;
    private final ActionUserContextHolder actionUserContextHolder;

    @Autowired
    public ProductController(ProductService productService, ActionUserContextHolder actionUserContextHolder) {
        this.productService = productService;
        this.actionUserContextHolder = actionUserContextHolder;
    }

    @GetMapping
    @ApiResponse(
            description = "List of all products",
            responseCode = "200")
    public ResponseEntity<List<Product>> getAllProducts(
            @RequestParam(required = false)
            String productFamilyName,
            @RequestParam(required = false)
            Optional<Boolean> active) {
        List<Product> products = productService.getAllProducts(active, productFamilyName);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "Product found",
                    responseCode = "200"),
            @ApiResponse(
                    description = "Product not found",
                    responseCode = "404",
                    content = @Content(
                            schema = @Schema))
    })
    public ResponseEntity<Product> getProductById(
            @PathVariable
            long id) {
        return productService.getProductById(id)
                .map(product -> new ResponseEntity<>(product, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ApiResponse(
            description = "Product created",
            responseCode = "201")
    public ResponseEntity<Product> createProduct(
            @RequestBody
            SetProductRequest createProductRequest,
            @RequestParam(required = false)
            long actionUserId) {
        this.actionUserContextHolder.setActionUserId(actionUserId);
        Product product = productService.setProductFromRequest(createProductRequest, null);
        product = productService.saveProduct(product);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "Product updated succesfully",
                    responseCode = "200"),
            @ApiResponse(
                    description = "Product not found",
                    responseCode = "404", content = @Content(schema = @Schema()))
    })
    public ResponseEntity<Product> updateProduct(
            @PathVariable
            long id,
            @RequestBody
            SetProductRequest updateProductRequest,
            @RequestParam(required = false)
            long actionUserId) {
        this.actionUserContextHolder.setActionUserId(actionUserId);
        Product product = productService.getProductById(id).orElse(null);
        if (product == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        product = productService.setProductFromRequest(updateProductRequest, product);
        product = productService.saveProduct(product);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable long id) {

        if (productService.getProductById(id).isPresent()) {
            productService.deleteProduct(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}