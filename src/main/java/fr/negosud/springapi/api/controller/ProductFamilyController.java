package fr.negosud.springapi.api.controller;

import fr.negosud.springapi.api.component.ActionUserContextHolder;
import fr.negosud.springapi.api.model.dto.SetProductFamilyRequest;
import fr.negosud.springapi.api.model.entity.ProductFamily;
import fr.negosud.springapi.api.service.ProductFamilyService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productFamily")
@Tag(name = "ProductFamily", description = "Endpoints related to ProductFamily crud and actions.")
public class ProductFamilyController {

    private final ProductFamilyService productFamilyService;

    private final ActionUserContextHolder actionUserContextHolder;

    @Autowired
    public ProductFamilyController(ProductFamilyService productFamilyService, ActionUserContextHolder actionUserContextHolder) {
        this.productFamilyService = productFamilyService;
        this.actionUserContextHolder = actionUserContextHolder;
    }

    @GetMapping
    @ApiResponse(
            description = "List of all productFamilies",
            responseCode = "200")
    public ResponseEntity<List<ProductFamily>> getAllProductFamilies() {
        List<ProductFamily> productFamilies = productFamilyService.getAllProductFamilies();
        return new ResponseEntity<>(productFamilies, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "ProductFamily found",
                    responseCode = "200"),
            @ApiResponse(
                    description = "ProductFamily not found",
                    responseCode = "404",
                    content = @Content(schema = @Schema))
    })
    public ResponseEntity<ProductFamily> getProductFamilyById(
            @PathVariable
            long id) {
        return productFamilyService.getProductFamilyById(id)
                .map(productFamily -> new ResponseEntity<>(productFamily, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(
                    description = "ProductFamily created",
                    responseCode = "201",
                    content = @Content(schema = @Schema(implementation = ProductFamily.class ))),
            @ApiResponse(
                    description = "ProductFamily processed name as code already exists",
                    responseCode = "409",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> createProductFamily(
            @RequestBody
            SetProductFamilyRequest createProductFamilyRequest,
            @RequestParam(required = false)
            long actionUserId) {
        this.actionUserContextHolder.setActionUserId(actionUserId);
        ProductFamily productFamily = productFamilyService.setProductFamilyFromRequest(createProductFamilyRequest, null);
        try {
            productFamilyService.saveProductFamily(productFamily);
        } catch (DuplicateKeyException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(productFamily, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "ProductFamily updated successfully",
                    responseCode = "200"),
            @ApiResponse(
                    description = "ProductFamily not found",
                    responseCode = "404", content = @Content(schema = @Schema))
    })
    public ResponseEntity<ProductFamily> updateProductFamily(
            @PathVariable
            long id,
            @RequestBody
            SetProductFamilyRequest updateProductFamilyRequest,
            @RequestParam(required = false)
            long actionUserId) {
        ProductFamily productFamily = productFamilyService.getProductFamilyById(id).orElse(null);
        if (productFamily == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        actionUserContextHolder.setActionUserId(actionUserId);
        productFamilyService.setProductFamilyFromRequest(updateProductFamilyRequest, productFamily);
        productFamilyService.saveProductFamily(productFamily);
        return new ResponseEntity<>(productFamily, HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "ProductFamily deleted",
                    responseCode = "204"),
            @ApiResponse(
                    description = "ProductFamily not found",
                    responseCode = "404"),
            @ApiResponse(
                    description = "ProductFamily can't be deleted",
                    responseCode = "403",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> deleteProductFamily(
            @PathVariable
            long id,
            @RequestBody(required = false)
            long replacedByProductFamilyId,
            @RequestParam(required = false)
            long actionUserId) {
        ProductFamily productFamily = productFamilyService.getProductFamilyById(id).orElse(null);
        if (productFamily == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (productFamily.getProductList().isEmpty()) {
            productFamilyService.deleteProductFamily(productFamily);
        } else {
            actionUserContextHolder.setActionUserId(actionUserId);
            if (replacedByProductFamilyId < 1)
                return new ResponseEntity<>("replacedByProductFamilyId is needed as a valid Id", HttpStatus.FORBIDDEN);
            ProductFamily replacingProductFamily = productFamilyService.getProductFamilyById(replacedByProductFamilyId).orElse(null);
            if (replacingProductFamily == null)
                return new ResponseEntity<>("replacedByProductFamily doesn't correspond to a proper ProductFamily", HttpStatus.FORBIDDEN);
            productFamilyService.safeDeleteProductFamily(productFamily, replacingProductFamily);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
