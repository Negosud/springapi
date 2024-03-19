package fr.negosud.springapi.api.controller;

import fr.negosud.springapi.api.component.ActionUserContextHolder;
import fr.negosud.springapi.api.model.dto.SetProductTransactionTypeRequest;
import fr.negosud.springapi.api.model.entity.ProductTransactionType;
import fr.negosud.springapi.api.service.ProductTransactionTypeService;
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
import java.util.Optional;

@RestController
@RequestMapping("/product_transaction_type")
@Tag(name = "ProductTransactionType", description = "Endpoints related to ProductTransactionType crud and actions.")
public class ProductTransactionTypeController {

    final private ProductTransactionTypeService productTransactionTypeService;
    final private ActionUserContextHolder actionUserContextHolder;

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

    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "ProductTransactionType found",
                    responseCode = "200"),
            @ApiResponse(
                    description = "ProductTransactionType not found",
                    responseCode = "404",
                    content = @Content(schema = @Schema))
    })
    public ResponseEntity<ProductTransactionType> getProductTransactionTypeById(
            @PathVariable
            long id) {
        return productTransactionTypeService.getProductTransactionTypeById(id)
                .map(product -> new ResponseEntity<>(product, HttpStatus.OK))
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
            @RequestBody
            SetProductTransactionTypeRequest createProductTransactionTypeRequest,
            @RequestParam
            Long actionUserId) {
        this.actionUserContextHolder.setActionUserId(actionUserId);
        ProductTransactionType productTransactionType = productTransactionTypeService.setProductTransactionTypeFromRequest(createProductTransactionTypeRequest, null);
        try {
            productTransactionTypeService.saveProductTransactionType(productTransactionType);
        } catch (DuplicateKeyException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(productTransactionType, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
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
            long id,
            @RequestBody
            SetProductTransactionTypeRequest updateProductTransactionTypeRequest,
            @RequestParam
            Long actionUserId) {
        ProductTransactionType productTransactionType = productTransactionTypeService.getProductTransactionTypeById(id).orElse(null);
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

    @DeleteMapping("/{id}")
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
            long id,
            @RequestParam(required = false)
            Long replacedByProductTransactionTypeId,
            @RequestParam
            Long actionUserId) {
        ProductTransactionType productTransactionType = productTransactionTypeService.getProductTransactionTypeById(id).orElse(null);
        if (productTransactionType == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (productTransactionType.getProductTransactionList().isEmpty()) {
            productTransactionTypeService.deleteProductTransactionType(productTransactionType);
        } else {
            actionUserContextHolder.setActionUserId(actionUserId);
            if (replacedByProductTransactionTypeId < 1)
                return new ResponseEntity<>("replacedByProductTransactionTypeId is needed as a valid Id", HttpStatus.FORBIDDEN);
            ProductTransactionType replacingProductTransactionType = productTransactionTypeService.getProductTransactionTypeById(replacedByProductTransactionTypeId).orElse(null);
            if (replacingProductTransactionType == null)
                return new ResponseEntity<>("replacedByProductTransactionTypeId doesn't correspond to a proper ProductTransactionType", HttpStatus.FORBIDDEN);
            if (productTransactionType.isEntry() != replacingProductTransactionType.isEntry())
                return new ResponseEntity<>("ProductTransactionType can't be replaced by replacingProductTransactionType (invalid property isEntry)", HttpStatus.FORBIDDEN);
            productTransactionTypeService.safeDeleteProductTransactionType(productTransactionType, replacingProductTransactionType);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
