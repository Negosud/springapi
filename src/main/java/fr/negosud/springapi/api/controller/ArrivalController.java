package fr.negosud.springapi.api.controller;

import fr.negosud.springapi.api.component.ActionUserContextHolder;
import fr.negosud.springapi.api.model.dto.SetArrivalRequest;
import fr.negosud.springapi.api.model.entity.Arrival;
import fr.negosud.springapi.api.service.ArrivalService;
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
@RequestMapping("/arrival")
@Tag(name = "Arrival", description = "Endpoints related to Arrival crud and actions.")
public class ArrivalController {
    private final ArrivalService arrivalService;
    private final ActionUserContextHolder actionUserContextHolder;


    @Autowired
    public ArrivalController(ArrivalService arrivalService, ActionUserContextHolder actionUserContextHolder) {
        this.arrivalService = arrivalService;
        this.actionUserContextHolder = actionUserContextHolder;
    }

    @GetMapping
    @ApiResponse(description = "List of all arrivals", responseCode = "200")
    public ResponseEntity<List<Arrival>> getAllArrivals() {
        List<Arrival> arrivals = arrivalService.getAllArrivals();
        return new ResponseEntity<>(arrivals, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(description = "Arrival found", responseCode = "200"),
            @ApiResponse(description = "Arrival not found", responseCode = "404", content = {
                    @Content(schema = @Schema())
            })
    })
    public ResponseEntity<Arrival> getArrivalById(@PathVariable Long id) {
        return arrivalService.getArrivalById(id)
                .map(arrival -> new ResponseEntity<>(arrival, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    @ApiResponse(description = "Arrival updated", responseCode = "200")
    public ResponseEntity<Arrival> updateArrival(@PathVariable Long id, @RequestBody SetArrivalRequest updateArrivalRequest, @RequestParam(required = false)
    long actionUserId) {
        this.actionUserContextHolder.setActionUserId(actionUserId);
        Arrival arrival = arrivalService.getArrivalById(id).orElse(null);
        if(arrival == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            arrival = arrivalService.setArrivalFromRequest(updateArrivalRequest, arrival);
            arrival = arrivalService.saveArrival(arrival);
            return new ResponseEntity<>(arrival, HttpStatus.OK);
        }
    }

    @PostMapping
    @ApiResponse(description = "Arrival created", responseCode = "201")
    public ResponseEntity<Arrival> createArrival(
            @RequestBody
            SetArrivalRequest createdArrivalRequest,
            @RequestParam(required = false)
            long actionUserId) {
        this.actionUserContextHolder.setActionUserId(actionUserId);
        Arrival arrival = arrivalService.setArrivalFromRequest(createdArrivalRequest, null);
        arrival = arrivalService.saveArrival(arrival);
        return new ResponseEntity<>(arrival, HttpStatus.CREATED);
    }
}
