package fr.negosud.springapi.api.controller;

import fr.negosud.springapi.api.component.ActionUserContextHolder;
import fr.negosud.springapi.api.model.dto.CreateProductRequest;
import fr.negosud.springapi.api.model.dto.UpdateProductRequest;
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
        return new ResponseEntity<>(productService.getAllProducts(active, productFamilyName), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "Product found",
                    responseCode = "200"),
            @ApiResponse(
                    description = "Product not found",
                    responseCode = "404",
                    content = @Content(schema = @Schema))
    })
    public ResponseEntity<Product> getProductById(
            @PathVariable
            long id) {
        return productService.getProductById(id)
                .map(product -> new ResponseEntity<>(product, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(
                    description = "Product created",
                    responseCode = "201",
                    content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(
                    description = "Product can't be created",
                    responseCode = "403",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> createProduct(
            @RequestBody
            CreateProductRequest createProductRequest,
            @RequestParam
            Long actionUserId) {
        this.actionUserContextHolder.setActionUserId(actionUserId);
        try {
            return new ResponseEntity<>(productService.createProductFromRequest(createProductRequest), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "Product updated successfully",
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(
                    description = "Product can't be updated",
                    responseCode = "403",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(
                    description = "Product not found",
                    responseCode = "404",
                    content = @Content(schema = @Schema))
    })
    public ResponseEntity<?> updateProduct(
            @PathVariable
            long id,
            @RequestBody
            UpdateProductRequest updateProductRequest,
            @RequestParam
            Long actionUserId) {
        Product product = productService.getProductById(id).orElse(null);
        if (product == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        this.actionUserContextHolder.setActionUserId(actionUserId);
        try {
            return new ResponseEntity<>(productService.updateProductFromRequest(updateProductRequest, product), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }
}
