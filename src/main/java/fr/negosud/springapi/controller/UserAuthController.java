package fr.negosud.springapi.controller;

import fr.negosud.springapi.model.dto.request.LoginRequest;
import fr.negosud.springapi.model.dto.response.UserResponse;
import fr.negosud.springapi.model.entity.User;
import fr.negosud.springapi.service.UserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/auth")
@Tag(name = "User")
public class UserAuthController {

    private final UserService userService;

    @Autowired
    public UserAuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "User not found",
                    responseCode = "404"),
            @ApiResponse(
                    description = "User can't connect",
                    responseCode = "403",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(
                    description = "Login succesfully",
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = UserResponse.class)))
    })
    public ResponseEntity<?> loginAction(
            @Valid @RequestBody
            LoginRequest loginRequest) {
        User user = userService.getUserByLogin(loginRequest.getLogin()).orElse(null);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (!user.isActive())
            return new ResponseEntity<>("User isn't active", HttpStatus.FORBIDDEN);
        if (userService.matchUserPassword(user, loginRequest.getPassword()))
            return new ResponseEntity<>(userService.getResponseFromUser(user), HttpStatus.OK);
        return new ResponseEntity<>("Password isn't matching", HttpStatus.FORBIDDEN);
    }

}
