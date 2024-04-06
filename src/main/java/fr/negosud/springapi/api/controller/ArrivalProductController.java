package fr.negosud.springapi.api.controller;

import fr.negosud.springapi.api.component.ActionUserContextHolder;
import fr.negosud.springapi.api.model.entity.ArrivalProduct;
import fr.negosud.springapi.api.service.ArrivalProductService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/arrival_product")
@Tag(name = "Arrival Product", description = "Endpoints related to ArrivalProduct actions.")
public class ArrivalProductController {

    private final ArrivalProductService arrivalProductService;
    private final ActionUserContextHolder actionUserContextHolder;

    @Autowired
    public ArrivalProductController(ArrivalProductService arrivalProductService, ActionUserContextHolder actionUserContextHolder) {
        this.arrivalProductService = arrivalProductService;
        this.actionUserContextHolder = actionUserContextHolder;
    }

    @GetMapping("/unlinked")
    @ApiResponse(
            description = "List of ArrivalProducts that need to be linked to arrivals",
            responseCode = "200")
    public ResponseEntity<List<ArrivalProduct>> getUnlinkedArrivalProducts() {
        return new ResponseEntity<>(arrivalProductService.getUnlinkedArrivalProducts(), HttpStatus.OK);
    }
}
