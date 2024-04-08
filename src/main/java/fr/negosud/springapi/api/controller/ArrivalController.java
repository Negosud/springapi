package fr.negosud.springapi.api.controller;

import fr.negosud.springapi.api.component.ActionUserContextHolder;
import fr.negosud.springapi.api.model.ArrivalStatus;
import fr.negosud.springapi.api.model.dto.request.PlaceArrivalRequest;
import fr.negosud.springapi.api.model.dto.response.ArrivalResponse;
import fr.negosud.springapi.api.model.entity.Arrival;
import fr.negosud.springapi.api.service.ArrivalService;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
@RequestMapping("/arrival")
@Tag(name = "Arrival", description = "Endpoints related to Arrival crud and actions.")
public class ArrivalController {

    private final ArrivalService arrivalService;
    private final ActionUserContextHolder actionUserContextHolder;

    @Autowired
    public ArrivalController(ArrivalService arrivalService, ActionUserContextHolder contextHolder) {
        this.arrivalService = arrivalService;
        this.actionUserContextHolder = contextHolder;
    }

    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(
                    description = "List of all arrivals",
                    responseCode = "200",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ArrivalResponse.class)))),
            @ApiResponse(
                    description = "Entity not from request parameter",
                    responseCode = "404",
                    content = @Content(schema = @Schema(implementation = String.class))),
    })
    public ResponseEntity<?> getAllArrivals(
            @RequestParam(required = false)
            ArrivalStatus status,
            @RequestParam(required = false)
            Long suppliedById) {
        List<Arrival> arrivals;
        try {
            arrivals = arrivalService.getAllArrivals(status, suppliedById);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        List<ArrivalResponse> arrivalResponses = new ArrayList<>();
        for (Arrival arrival : arrivals) {
            ArrivalResponse arrivalResponse = arrivalService.getResponseFromArrival(arrival);
            arrivalResponses.add(arrivalResponse);
        }
        return new ResponseEntity<>(arrivalResponses, HttpStatus.OK);
    }

    @GetMapping("/{reference}")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "Arrival found",
                    responseCode = "200"),
            @ApiResponse(
                    description = "Arrival not found",
                    responseCode = "404",
                    content = @Content)
    })
    public ResponseEntity<ArrivalResponse> getArrivalByReference(
            @PathVariable
            String reference) {
        return arrivalService.getArrivalByReference(reference)
                .map(arrival -> new ResponseEntity<>(arrivalService.getResponseFromArrival(arrival), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(
                    description = "Arrival placed",
                    responseCode = "201",
                    content = @Content(schema = @Schema(implementation = ArrivalResponse.class))),
            @ApiResponse(
                    description = "Arrival can't be placed",
                    responseCode = "403",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> placeArrival(
            @Valid @RequestBody
            PlaceArrivalRequest placeArrivalRequest,
            @RequestParam
            Long actionUserId) {
        actionUserContextHolder.setActionUserId(actionUserId);
        return null;
    }

}
