package fr.negosud.springapi.controller;

import fr.negosud.springapi.component.ActionUserContextHolder;
import fr.negosud.springapi.model.ArrivalStatus;
import fr.negosud.springapi.model.dto.request.PlaceArrivalRequest;
import fr.negosud.springapi.model.dto.response.ArrivalResponse;
import fr.negosud.springapi.model.dto.response.OrderResponse;
import fr.negosud.springapi.model.entity.Arrival;
import fr.negosud.springapi.service.ArrivalService;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
                    description = "Entity not found from request parameter",
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

    @PatchMapping("/{reference}/order")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "Arrival has been ordered",
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = ArrivalResponse.class))),
            @ApiResponse(
                    description = "Arrival can't be ordered",
                    responseCode = "403",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(description = "Arrival not found",
                    responseCode = "404",
                    content = @Content)
    })
    public ResponseEntity<?> orderArrival(
            @PathVariable
            String reference,
            @RequestParam
            Long actionUserId,
            @RequestBody @Valid @NotBlank @Size(max = 1000)
            String comment) {
        Arrival arrival = arrivalService.getArrivalByReference(reference).orElse(null);
        if (arrival == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        actionUserContextHolder.setActionUserId(actionUserId);
        try {
            assert arrival.getStatus() == ArrivalStatus.ORDERING : "Arrival isn't in the ORDERING state";
            arrival.setStatus(ArrivalStatus.PENDING);
            arrival.setComment(comment);
            arrivalService.saveArrival(arrival);
            return new ResponseEntity<>(arrivalService.getResponseFromArrival(arrival), HttpStatus.OK);
        } catch (AssertionError error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.FORBIDDEN);
        }
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
        try {
            Arrival arrival = arrivalService.placeArrivalFromRequest(placeArrivalRequest);
            return new ResponseEntity<>(arrivalService.getResponseFromArrival(arrival), HttpStatus.OK);
        } catch (AssertionError e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @PatchMapping("/{reference}/incoming")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "Arrival has been marked as incoming",
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = ArrivalResponse.class))),
            @ApiResponse(
                    description = "Arrival can't be marked as incoming",
                    responseCode = "403",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(description = "Arrival not found",
                    responseCode = "404",
                    content = @Content)
    })
    public ResponseEntity<?> markArrivalAsIncoming(
            @PathVariable
            String reference,
            @RequestParam
            Long actionUserId,
            @RequestBody @Valid @NotBlank @Size(max = 1000)
            String comment) {
        Arrival arrival = arrivalService.getArrivalByReference(reference).orElse(null);
        if (arrival == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        actionUserContextHolder.setActionUserId(actionUserId);
        try {
            assert arrival.getStatus() == ArrivalStatus.PENDING : "Arrival isn't in the PENDING state";
            arrival.setStatus(ArrivalStatus.INCOMING);
            arrival.setComment(comment);
            arrivalService.saveArrival(arrival);
            return new ResponseEntity<>(arrivalService.getResponseFromArrival(arrival), HttpStatus.OK);
        } catch (AssertionError error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @PatchMapping("/{reference}/cancel")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "Arrival has been canceled",
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(
                    description = "Arrival can't be canceled",
                    responseCode = "403",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(description = "Arrival not found",
                    responseCode = "404",
                    content = @Content)
    })
    public ResponseEntity<?> cancelArrival(
            @PathVariable
            String reference,
            @RequestBody @Valid @NotBlank @Size(max = 1000)
            String comment,
            @RequestParam Long actionUserId) {
        Arrival arrival = arrivalService.getArrivalByReference(reference).orElse(null);
        if (arrival == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        actionUserContextHolder.setActionUserId(actionUserId);
        try {
            arrivalService.cancelArrival(arrival);
            arrival.setComment(comment);
            arrivalService.saveArrival(arrival);
        } catch (AssertionError e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(arrivalService.getResponseFromArrival(arrival), HttpStatus.OK);
    }

    @PatchMapping("/{reference}/complete")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "Arrival has been completed",
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = ArrivalResponse.class))),
            @ApiResponse(
                    description = "Arrival can't be completed",
                    responseCode = "403",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(description = "Arrival not found",
                    responseCode = "404",
                    content = @Content),
            @ApiResponse(description = "Internal Server Error",
                    responseCode = "500",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> completeArrival(
            @PathVariable
            String reference,
            @RequestParam
            Long actionUserId,
            @RequestBody @Valid @NotBlank @Size(max = 1000)
            String comment) {
        Arrival arrival = arrivalService.getArrivalByReference(reference).orElse(null);
        if (arrival == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        actionUserContextHolder.setActionUserId(actionUserId);
        try {
            assert arrival.getStatus() == ArrivalStatus.INCOMING : "Arrival isn't in the INCOMING state";
            arrivalService.completeArrival(arrival);
            arrival.setComment(comment);
            arrivalService.saveArrival(arrival);
            return new ResponseEntity<>(arrivalService.getResponseFromArrival(arrival), HttpStatus.OK);
        } catch (AssertionError e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
