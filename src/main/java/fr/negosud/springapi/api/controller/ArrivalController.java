package fr.negosud.springapi.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/arrival")
@Tag(name = "Arrival", description = "Endpoints related to Arrival crud and actions.")
public class ArrivalController {
}
