package fr.negosud.springapi.controller;

import fr.negosud.springapi.component.ActionUserContextHolder;
import fr.negosud.springapi.model.entity.SupplierProduct;
import fr.negosud.springapi.service.SupplierProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping //TODO
    public List<SupplierProduct> getAllSupplierProducts() {
        return null;
    }
}
