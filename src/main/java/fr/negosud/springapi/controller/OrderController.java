package fr.negosud.springapi.controller;

import fr.negosud.springapi.component.ActionUserContextHolder;
import fr.negosud.springapi.model.OrderStatus;
import fr.negosud.springapi.model.dto.request.PlaceOrderRequest;
import fr.negosud.springapi.model.dto.response.OrderResponse;
import fr.negosud.springapi.model.entity.Order;
import fr.negosud.springapi.service.OrderService;
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

import java.util.ArrayList;
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
    public ResponseEntity<List<OrderResponse>> getAllOrders(
            @Valid @RequestParam(required = false)
            OrderStatus status) {
        List<Order> orders = orderService.getAllOrders(status);
        List<OrderResponse> orderResponses = new ArrayList<>();
        for (Order order : orders) {
            OrderResponse orderResponse = orderService.getResponseFromOrder(order);
            orderResponses.add(orderResponse);
        }
        return new ResponseEntity<>(orderResponses, HttpStatus.OK);
    }

    @GetMapping("/{reference}")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "Order found",
                    responseCode = "200"),
            @ApiResponse(
                    description = "Order not found",
                    responseCode = "404",
                    content = @Content)
    })
    public ResponseEntity<OrderResponse> getOrderByReference(
            @PathVariable
            String reference) {
        return orderService.getOrderByReference(reference)
                .map(order -> new ResponseEntity<>(orderService.getResponseFromOrder(order), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(
                    description = "Order placed",
                    responseCode = "201",
                    content = @Content(schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(
                    description = "Order can't be placed",
                    responseCode = "403",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> placeOrder(
            @Valid @RequestBody
            PlaceOrderRequest placeOrderRequest,
            @RequestParam
            Long actionUserId) {
        actionUserContextHolder.setActionUserId(actionUserId);
        try {
            Order order = orderService.placeOrderFromRequest(placeOrderRequest);
            return new ResponseEntity<>(orderService.getResponseFromOrder(order), HttpStatus.CREATED);
        } catch (AssertionError e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @PatchMapping("/{reference}/prepare")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "Order has been prepared",
                    responseCode = "200"),
            @ApiResponse(description = "Order not found",
                    responseCode = "404",
                    content = @Content)
    })
    public ResponseEntity<OrderResponse> prepareOrder(
            @PathVariable
            String reference,
            @RequestParam
            Long actionUserId) {
        Order order = orderService.getOrderByReference(reference).orElse(null);
        if (order == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        actionUserContextHolder.setActionUserId(actionUserId);
        orderService.markAsReady(order);
        return new ResponseEntity<>(orderService.getResponseFromOrder(order), HttpStatus.OK);
    }

    @PatchMapping("/{reference}/cancel")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "Order has been canceled",
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(
                    description = "Order can't be canceled",
                    responseCode = "403",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(description = "Order not found",
                    responseCode = "404",
                    content = @Content)
    })
    public ResponseEntity<?> cancelOrder(
            @PathVariable
            String reference,
            @RequestParam
            Long actionUserId) {
        Order order = orderService.getOrderByReference(reference).orElse(null);
        if (order == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        actionUserContextHolder.setActionUserId(actionUserId);
        try {
            orderService.cancelOrder(order);
        } catch (AssertionError e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(orderService.getResponseFromOrder(order), HttpStatus.OK);
    }

    @PatchMapping("/{reference}/complete")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "Order has been completeed",
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(
                    description = "Order can't be completeed",
                    responseCode = "403",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(description = "Order not found",
                    responseCode = "404",
                    content = @Content)
    })
    public ResponseEntity<?> completeOrder(
            @PathVariable
            String reference,
            @RequestParam
            Long actionUserId) {
        Order order = orderService.getOrderByReference(reference).orElse(null);
        if (order == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        actionUserContextHolder.setActionUserId(actionUserId);
        try {
            orderService.completeOrder(order);
        } catch (AssertionError e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(orderService.getResponseFromOrder(order), HttpStatus.OK);
    }
}
