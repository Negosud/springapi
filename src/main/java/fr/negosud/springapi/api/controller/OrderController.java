package fr.negosud.springapi.api.controller;

import fr.negosud.springapi.api.component.ActionUserContextHolder;
import fr.negosud.springapi.api.model.dto.OrderStatus;
import fr.negosud.springapi.api.model.dto.PlaceOrderRequest;
import fr.negosud.springapi.api.model.entity.Order;
import fr.negosud.springapi.api.service.OrderService;
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
@RequestMapping("/order")
@Tag(name = "Order", description = "Endpoints related to Order crud and actions.")
public class OrderController {

    private final OrderService orderService;
    private final ActionUserContextHolder actionUserContextHolder;

    @Autowired
    public OrderController(OrderService orderService, ActionUserContextHolder actionUserContextHolder) {
        this.orderService = orderService;
        this.actionUserContextHolder = actionUserContextHolder;
    }

    @GetMapping
    @ApiResponse(
            description = "List of all orders",
            responseCode = "200")
    public ResponseEntity<List<Order>> getAllOrders(
            @RequestParam(required = false)
            OrderStatus status) {
        return new ResponseEntity<>(orderService.getAllOrders(status), HttpStatus.OK);
    }

    @GetMapping("/{reference}")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "Order found",
                    responseCode = "200"),
            @ApiResponse(
                    description = "Order not found",
                    responseCode = "404",
                    content = @Content(schema = @Schema))
    })
    public ResponseEntity<Order> getOrderByReference(
            @PathVariable
            String reference) {
        return orderService.getOrderByReference(reference)
                .map(order -> new ResponseEntity<>(order, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(
                    description = "Order placed",
                    responseCode = "201",
                    content = @Content(schema = @Schema(implementation = Order.class))),
            @ApiResponse(
                    description = "Order can't be placed",
                    responseCode = "403",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> placeOrder(
            @RequestBody
            PlaceOrderRequest placeOrderRequest,
            @RequestParam
            Long actionUserId) {
        this.actionUserContextHolder.setActionUserId(actionUserId);
        try {
            return new ResponseEntity<>(orderService.placeOrderFromRequest(placeOrderRequest), HttpStatus.CREATED);
        } catch (AssertionError e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }
}
