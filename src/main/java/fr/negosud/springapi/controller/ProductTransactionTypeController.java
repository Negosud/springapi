package fr.negosud.springapi.controller;

import fr.negosud.springapi.component.ActionUserContextHolder;
import fr.negosud.springapi.model.dto.request.SetProductTransactionTypeRequest;
import fr.negosud.springapi.model.entity.ProductTransactionType;
import fr.negosud.springapi.service.ProductTransactionTypeService;
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
import java.util.Optional;

@RestController
@RequestMapping("/product_transaction_type")
@Tag(name = "ProductTransactionType", description = "Endpoints related to ProductTransactionType crud and actions.")
public class ProductTransactionTypeController {

    private final ProductTransactionTypeService productTransactionTypeService;
    private final ActionUserContextHolder actionUserContextHolder;

    @Autowired
    public ProductTransactionTypeController(ProductTransactionTypeService productTransactionTypeService, ActionUserContextHolder actionUserContextHolder) {
        this.productTransactionTypeService =  productTransactionTypeService;
        this.actionUserContextHolder = actionUserContextHolder;
    }

    @GetMapping
    @ApiResponse(
            description = "List of all ProductTranstionTypes",
            responseCode = "200")
    public ResponseEntity<List<ProductTransactionType>> getAllProductTransactionTypes(
            @RequestParam
            Optional<Boolean> isEntry) {
        List<ProductTransactionType> productTransactionTypes = productTransactionTypeService.getAllProductTransactionType(isEntry.orElse(null));
        return new ResponseEntity<>(productTransactionTypes, HttpStatus.OK);
    }

    @GetMapping("/{code}")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "ProductTransactionType found",
                    responseCode = "200"),
            @ApiResponse(
                    description = "ProductTransactionType not found",
                    responseCode = "404",
                    content = @Content)
    })
    public ResponseEntity<ProductTransactionType> getProductTransactionTypeByCode(
            @PathVariable
            String code) {
        return productTransactionTypeService.getProductTransactionTypeByCode(code)
                .map(productTransactionType -> new ResponseEntity<>(productTransactionType, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping()
    @ApiResponses(value = {
            @ApiResponse(
                    description = "ProductTransactionType created",
                    responseCode = "201",
                    content = @Content(schema = @Schema(implementation = ProductTransactionType.class ))),
            @ApiResponse(
                    description = "ProductTransactionType processed name as code already exists",
                    responseCode = "409",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> createProductTransactionType(
            @Valid @RequestBody
            SetProductTransactionTypeRequest createProductTransactionTypeRequest,
            @RequestParam
            Long actionUserId) {
        actionUserContextHolder.setActionUserId(actionUserId);
        ProductTransactionType productTransactionType = productTransactionTypeService.setProductTransactionTypeFromRequest(createProductTransactionTypeRequest, null);
        try {
            productTransactionTypeService.saveProductTransactionType(productTransactionType);
        } catch (DuplicateKeyException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(productTransactionType, HttpStatus.CREATED);
    }

    @PutMapping("/{code}")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "ProductTransactionType updated successfully",
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = ProductTransactionType.class))),
            @ApiResponse(
                    description = "ProductTransactionType not found",
                    responseCode = "404"),
            @ApiResponse(
                    description = "ProductTransactionType isEntry field cannot be changed",
                    responseCode = "409",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> updateProductTransactionType(
            @PathVariable
            String code,
            @Valid @RequestBody
            SetProductTransactionTypeRequest updateProductTransactionTypeRequest,
            @RequestParam
            Long actionUserId) {
        ProductTransactionType productTransactionType = productTransactionTypeService.getProductTransactionTypeByCode(code).orElse(null);
        if (productTransactionType == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        actionUserContextHolder.setActionUserId(actionUserId);
        try {
            productTransactionTypeService.setProductTransactionTypeFromRequest(updateProductTransactionTypeRequest, productTransactionType);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
        productTransactionTypeService.saveProductTransactionType(productTransactionType);
        return new ResponseEntity<>(productTransactionType, HttpStatus.OK);
    }

    @DeleteMapping("/{code}")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "ProductTransactionType deleted",
                    responseCode = "204"),
            @ApiResponse(
                    description = "ProductTransactionType not found",
                    responseCode = "404"),
            @ApiResponse(
                    description = "ProductTransactionType can't be deleted",
                    responseCode = "403",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> deleteProductTransactionType(
            @PathVariable
            String code,
            @RequestParam(required = false)
            String replacedByProductTransactionTypeCode,
            @RequestParam
            Long actionUserId) {
        ProductTransactionType productTransactionType = productTransactionTypeService.getProductTransactionTypeByCode(code).orElse(null);
        if (productTransactionType == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (!productTransactionType.isRemovable())
            return new ResponseEntity<>("ProductTransactionType can't be removed (used by the system)", HttpStatus.FORBIDDEN);
        if (productTransactionType.getProductTransactions().isEmpty()) {
            productTransactionTypeService.deleteProductTransactionType(productTransactionType);
        } else {
            actionUserContextHolder.setActionUserId(actionUserId);
            if (replacedByProductTransactionTypeCode.isBlank())
                return new ResponseEntity<>("replacedByProductTransactionTypeCode is needed as a valid code", HttpStatus.FORBIDDEN);
            ProductTransactionType replacingProductTransactionType = productTransactionTypeService.getProductTransactionTypeByCode(replacedByProductTransactionTypeCode).orElse(null);
            if (replacingProductTransactionType == null)
                return new ResponseEntity<>("replacedByProductTransactionTypeCode doesn't correspond to a proper ProductTransactionType", HttpStatus.FORBIDDEN);
            if (productTransactionType.isEntry() != replacingProductTransactionType.isEntry())
                return new ResponseEntity<>("ProductTransactionType can't be replaced by replacingProductTransactionType (invalid property isEntry)", HttpStatus.FORBIDDEN);
            productTransactionTypeService.safeDeleteProductTransactionType(productTransactionType, replacingProductTransactionType);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
