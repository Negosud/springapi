package fr.negosud.springapi.api.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/get")
    public String getUser(@RequestParam String name) {
        return name;
    }
}
