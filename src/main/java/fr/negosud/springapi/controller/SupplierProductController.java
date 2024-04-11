package fr.negosud.springapi.controller;

import fr.negosud.springapi.component.ActionUserContextHolder;
import fr.negosud.springapi.model.entity.SupplierProduct;
import fr.negosud.springapi.model.entity.composite.SupplierProductKey;
import fr.negosud.springapi.service.SupplierProductService;
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

@RestController
@RequestMapping("/supplier-product")
@Tag(name = "SupplierProduct", description = "Endpoints related to SupplierProduct crud and actions.")
public class SupplierProductController {

    private final SupplierProductService supplierProductService;
    private final ActionUserContextHolder actionUserContextHolder;

    @Autowired
    public SupplierProductController(SupplierProductService supplierProductService, ActionUserContextHolder actionUserContextHolder) {
        this.supplierProductService = supplierProductService;
        this.actionUserContextHolder = actionUserContextHolder;
    }

    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(
                    description = "List of all SupplierProducts",
                    responseCode = "200"),
            @ApiResponse(
                    description = "Entity not found from request parameter",
                    responseCode = "404",
                    content = @Content)
    })
    public ResponseEntity<List<SupplierProduct>> getAllSupplierProducts(
            @RequestParam(required = false)
            Long supplierId,
            @RequestParam(required = false)
            Long productId) {
        try {
            List<SupplierProduct> supplierProducts = supplierProductService.getAllSupplierProducts(supplierId, productId);
            return new ResponseEntity<>(supplierProducts, HttpStatus.OK);
        } catch (AssertionError e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{supplierId}/{productId}")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "SupplierProduct found",
                    responseCode = "200"),
            @ApiResponse(
                    description = "SupplierProduct not found",
                    responseCode = "404",
                    content = @Content)
    })
    public ResponseEntity<SupplierProduct> getSupplierProductById(
            @PathVariable
            Long supplierId,
            @PathVariable
            Long productId) {
        return supplierProductService.getSupplierProductById(new SupplierProductKey(supplierId, productId))
                .map(supplierProduct -> new ResponseEntity<>(supplierProduct, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(
                    description = "SupplierProduct set",
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = SupplierProduct.class ))),
            @ApiResponse(description = "Can't set SupplierProduct")
    })
    public ResponseEntity<SupplierProduct> setSupplierProduct(@RequestBody SupplierProduct supplierProduct) {
        return null;
    }
}
