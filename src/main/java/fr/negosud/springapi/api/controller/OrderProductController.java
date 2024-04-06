package fr.negosud.springapi.api.controller;

import fr.negosud.springapi.api.component.ActionUserContextHolder;
import fr.negosud.springapi.api.model.entity.OrderProduct;
import fr.negosud.springapi.api.service.OrderProductService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order_product")
@Tag(name = "OrderProduct", description = "Endpoints related to OrderProduct actions.")
public class OrderProductController {

    private final OrderProductService orderProductService;
    private final ActionUserContextHolder actionUserContextHolder;

    @Autowired
    public OrderProductController (OrderProductService orderProductService, ActionUserContextHolder actionUserContextHolder) {
        this.orderProductService = orderProductService;
        this.actionUserContextHolder = actionUserContextHolder;
    }

    @PatchMapping("/{id}/prepare")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "OrderProduct has been prepared",
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = OrderProduct.class))),
            @ApiResponse(description = "OrderProduct can't be prepared",
                    responseCode = "409",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(description = "OrderProduct not found",
                    responseCode = "404",
                    content = @Content)
    })
    public ResponseEntity<?> prepareOrderProduct(
            @PathVariable
            long id,
            @RequestParam
            Long actionUserId) {
        OrderProduct orderProduct = orderProductService.getOrderProductById(id).orElse(null);
        if (orderProduct == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        actionUserContextHolder.setActionUserId(actionUserId);
        try {
            orderProductService.markAsReady(orderProduct);
        } catch (AssertionError e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(orderProduct, HttpStatus.OK);
    }
}
