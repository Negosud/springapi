package fr.negosud.springapi.controller;

import fr.negosud.springapi.component.ActionUserContextHolder;
import fr.negosud.springapi.model.dto.request.CreateProductRequest;
import fr.negosud.springapi.model.dto.request.UpdateProductRequest;
import fr.negosud.springapi.model.dto.response.ProductResponse;
import fr.negosud.springapi.model.entity.Product;
import fr.negosud.springapi.service.ProductService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
            description = "List of all Products",
            responseCode = "200")
    public ResponseEntity<List<ProductResponse>> getAllProducts(
            @RequestParam(required = false)
            String productFamilyName,
            @RequestParam(required = false)
            Optional<Boolean> active) {
        List<Product> products = productService.getAllProducts(active, productFamilyName);
        List<ProductResponse> productResponses = new ArrayList<>();
        for (Product product : products) {
            ProductResponse productResponse = productService.getResponseFromProduct(product);
            productResponses.add(productResponse);
        }
        return new ResponseEntity<>(productResponses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "Product found",
                    responseCode = "200"),
            @ApiResponse(
                    description = "Product not found",
                    responseCode = "404",
                    content = @Content)
    })
    public ResponseEntity<ProductResponse> getProductById(
            @PathVariable
            long id) {
        return productService.getProductById(id)
                .map(product -> new ResponseEntity<>(productService.getResponseFromProduct(product), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(
                    description = "Product created",
                    responseCode = "201",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(
                    description = "Product can't be created",
                    responseCode = "403",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> createProduct(
            @Valid @RequestBody
            CreateProductRequest createProductRequest,
            @RequestParam
            Long actionUserId) {
        actionUserContextHolder.setActionUserId(actionUserId);
        try {
            Product product = productService.createProductFromRequest(createProductRequest);
            return new ResponseEntity<>(productService.getResponseFromProduct(product), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "Product updated successfully",
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(
                    description = "Product can't be updated",
                    responseCode = "403",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(
                    description = "Product not found",
                    responseCode = "404",
                    content = @Content)
    })
    public ResponseEntity<?> updateProduct(
            @PathVariable
            long id,
            @Valid @RequestBody
            UpdateProductRequest updateProductRequest,
            @RequestParam
            Long actionUserId) {
        Product product = productService.getProductById(id).orElse(null);
        if (product == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        actionUserContextHolder.setActionUserId(actionUserId);
        try {
            product = productService.updateProductFromRequest(updateProductRequest, product);
            return new ResponseEntity<>(productService.getResponseFromProduct(product), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }
}
