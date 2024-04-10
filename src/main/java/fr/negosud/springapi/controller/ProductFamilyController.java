package fr.negosud.springapi.controller;

import fr.negosud.springapi.component.ActionUserContextHolder;
import fr.negosud.springapi.model.dto.request.SetProductFamilyRequest;
import fr.negosud.springapi.model.entity.ProductFamily;
import fr.negosud.springapi.service.ProductFamilyService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

    @GetMapping("/{code}")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "ProductFamily found",
                    responseCode = "200"),
            @ApiResponse(
                    description = "ProductFamily not found",
                    responseCode = "404",
                    content = @Content)
    })
    public ResponseEntity<ProductFamily> getProductFamilyByCode(
            @PathVariable
            String code) {
        return productFamilyService.getProductFamilyByCode(code)
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
            @Valid @RequestBody
            SetProductFamilyRequest createProductFamilyRequest,
            @RequestParam
            Long actionUserId) {
        actionUserContextHolder.setActionUserId(actionUserId);
        ProductFamily productFamily = productFamilyService.setProductFamilyFromRequest(createProductFamilyRequest, null);
        try {
            productFamilyService.saveProductFamily(productFamily);
        } catch (DuplicateKeyException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(productFamily, HttpStatus.CREATED);
    }

    @PutMapping("/{code}")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "ProductFamily updated successfully",
                    responseCode = "200"),
            @ApiResponse(
                    description = "ProductFamily not found",
                    responseCode = "404", content = @Content)
    })
    public ResponseEntity<ProductFamily> updateProductFamily(
            @PathVariable
            String code,
            @Valid @RequestBody
            SetProductFamilyRequest updateProductFamilyRequest,
            @RequestParam
            Long actionUserId) {
        ProductFamily productFamily = productFamilyService.getProductFamilyByCode(code).orElse(null);
        if (productFamily == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        actionUserContextHolder.setActionUserId(actionUserId);
        productFamilyService.setProductFamilyFromRequest(updateProductFamilyRequest, productFamily);
        productFamilyService.saveProductFamily(productFamily);
        return new ResponseEntity<>(productFamily, HttpStatus.OK);

    }

    @DeleteMapping("/{code}")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "ProductFamily deleted",
                    responseCode = "204"),
            @ApiResponse(
                    description = "ProductFamily can't be deleted",
                    responseCode = "403",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(
                    description = "ProductFamily not found",
                    responseCode = "404")
    })
    public ResponseEntity<?> deleteProductFamily(
            @PathVariable
            String code,
            @RequestParam(required = false)
            String replacedByProductFamilyCode,
            @RequestParam
            Long actionUserId) {
        ProductFamily productFamily = productFamilyService.getProductFamilyByCode(code).orElse(null);
        if (productFamily == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (productFamily.getProducts().isEmpty()) {
            productFamilyService.deleteProductFamily(productFamily);
        } else {
            actionUserContextHolder.setActionUserId(actionUserId);
            if (replacedByProductFamilyCode.isBlank())
                return new ResponseEntity<>("replacedByProductFamilyCode is needed as a valid Code", HttpStatus.FORBIDDEN);
            ProductFamily replacingProductFamily = productFamilyService.getProductFamilyByCode(replacedByProductFamilyCode).orElse(null);
            if (replacingProductFamily == null)
                return new ResponseEntity<>("replacedByProductFamilyId doesn't correspond to a proper ProductFamily", HttpStatus.FORBIDDEN);
            productFamilyService.safeDeleteProductFamily(productFamily, replacingProductFamily);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
