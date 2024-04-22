package fr.negosud.springapi.controller;

import fr.negosud.springapi.component.ActionUserContextHolder;
import fr.negosud.springapi.model.dto.request.SetSupplierProductRequest;
import fr.negosud.springapi.model.entity.Product;
import fr.negosud.springapi.model.entity.SupplierProduct;
import fr.negosud.springapi.model.entity.User;
import fr.negosud.springapi.model.entity.composite.SupplierProductKey;
import fr.negosud.springapi.service.ProductService;
import fr.negosud.springapi.service.SupplierProductService;
import fr.negosud.springapi.service.UserService;
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

import java.util.List;

@RestController
@RequestMapping("/supplier-product")
@Tag(name = "SupplierProduct", description = "Endpoints related to SupplierProduct crud and actions.")
public class SupplierProductController {

    private final SupplierProductService supplierProductService;
    private final ActionUserContextHolder actionUserContextHolder;
    private final UserService userService;
    private final ProductService productService;

    @Autowired
    public SupplierProductController(SupplierProductService supplierProductService, ActionUserContextHolder actionUserContextHolder, UserService userService, ProductService productService) {
        this.supplierProductService = supplierProductService;
        this.actionUserContextHolder = actionUserContextHolder;
        this.userService = userService;
        this.productService = productService;
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
                    content = @Content(schema = @Schema(implementation = SupplierProduct.class))),
            @ApiResponse(description = "Can't set SupplierProduct",
                    responseCode = "403",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(description = "Supplier or product not found",
                    responseCode = "404",
                    content = @Content(schema = @Schema(implementation = String.class))),
    })
    public ResponseEntity<?> setSupplierProduct(
            @Valid @RequestBody
            SetSupplierProductRequest setSupplierProductRequest,
            @RequestParam
            Long actionUserId) {
        actionUserContextHolder.setActionUserId(actionUserId);
        if (!ActionUserContextHolder.can("desktop.supplier.set-product"))
            return new ResponseEntity<>("Missing permission desktop.supplier.set-product", HttpStatus.FORBIDDEN);
        User supplier = userService.getUserById(setSupplierProductRequest.getSupplierId()).orElse(null);
        if (supplier == null)
            return new ResponseEntity<>("Supplier not found", HttpStatus.NOT_FOUND);
        if (!supplier.can("supply"))
            return new ResponseEntity<>("Supplier can't supply", HttpStatus.FORBIDDEN);
        Product product = productService.getProductById(setSupplierProductRequest.getProductId()).orElse(null);
        if (product == null)
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        if (!product.isActive())
            return new ResponseEntity<>("Product is not active", HttpStatus.FORBIDDEN);
        if (product.getUnitPrice().compareTo(setSupplierProductRequest.getUnitPrice()) < 0)
            return new ResponseEntity<>("Supplier sell price is higher than product sell price", HttpStatus.FORBIDDEN);
        if (setSupplierProductRequest.getQuantity() < 0)
            return new ResponseEntity<>("Product suppliable quantity can't be negative", HttpStatus.FORBIDDEN);
        SupplierProduct supplierProduct = supplierProductService.getSupplierProductById(new SupplierProductKey(supplier.getId(), product.getId())).orElse(null);
        if (setSupplierProductRequest.getQuantity() == 0) {
            if (supplierProduct != null)
                supplierProductService.deleteSupplierProduct(supplierProduct);
            return new ResponseEntity<>("Supplier doesn't supply the product (anymore)", HttpStatus.OK);
        }
        if (supplierProduct == null)
            supplierProduct = new SupplierProduct().setId(new SupplierProductKey(supplier.getId(), product.getId()));

        supplierProduct.setSupplier(supplier)
                .setProduct(product)
                .setQuantity(setSupplierProductRequest.getQuantity())
                .setUnitPrice(setSupplierProductRequest.getUnitPrice());
        supplierProductService.saveSupplierProduct(supplierProduct);
        return new ResponseEntity<>(supplierProduct, HttpStatus.OK);
    }
}
