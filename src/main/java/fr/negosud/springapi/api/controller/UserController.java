package fr.negosud.springapi.api.controller;

import fr.negosud.springapi.api.component.ActionUserContextHolder;
import fr.negosud.springapi.api.model.dto.request.SetUserRequest;
import fr.negosud.springapi.api.model.dto.response.UserResponse;
import fr.negosud.springapi.api.model.entity.User;
import fr.negosud.springapi.api.service.UserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@Tag(name = "User", description = "Endpoints related to User crud and actions.")
public class UserController {

    private final UserService userService;
    private final ActionUserContextHolder actionUserContextHolder;

    @Autowired
    public UserController(UserService userService, ActionUserContextHolder actionUserContextHolder) {
        this.userService = userService;
        this.actionUserContextHolder = actionUserContextHolder;
    }

    @GetMapping
    @ApiResponse(
            description = "List of all users",
            responseCode = "200")
    public ResponseEntity<List<UserResponse>> getAllUsers(
            @RequestParam(required = false)
            String userGroupName,
            @RequestParam
            Optional<Boolean> active) {
        List<User> users = userService.getAllUsers(active, userGroupName);
        List<UserResponse> userResponses = new ArrayList<>();
        for (User user : users) {
            UserResponse userResponse = userService.getResponseFromUser(user);
            userResponses.add(userResponse);
        }
        return new ResponseEntity<>(userResponses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "User found",
                    responseCode = "200"),
            @ApiResponse(
                    description = "User not found",
                    responseCode = "404",
                    content = @Content)
    })
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable
            long id) {
        return userService.getUserById(id)
                .map(user -> new ResponseEntity<>(userService.getResponseFromUser(user), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ApiResponses(value =  {
            @ApiResponse(
                    description = "User created",
                    responseCode = "201",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(
                    description = "User can't be created",
                    responseCode = "403",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> createUser(
            @RequestBody
            SetUserRequest createUserRequest,
            @RequestParam
            Long actionUserId) {
        this.actionUserContextHolder.setActionUserId(actionUserId);
        User user;
        try {
            user = userService.setUserFromRequest(createUserRequest, null);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
        userService.saveUser(user);
        return new ResponseEntity<>(userService.getResponseFromUser(user), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "User updated successfully",
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(
                    description = "User can't be updated",
                    responseCode = "403",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(
                    description = "User not found",
                    responseCode = "404",
                    content = @Content)
    })
    public ResponseEntity<?> updateUser(
            @PathVariable
            long id,
            @RequestBody
            SetUserRequest updateUserRequest,
            @RequestParam
            Long actionUserId) {
        this.actionUserContextHolder.setActionUserId(actionUserId);
        User user = userService.getUserById(id).orElse(null);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        try {
            userService.setUserFromRequest(updateUserRequest, user);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
        userService.saveUser(user);
        return new ResponseEntity<>(userService.getResponseFromUser(user), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "User deleted",
                    responseCode = "204"),
            @ApiResponse(
                    description = "User not found",
                    responseCode = "404")
    })
    public ResponseEntity<Void> deleteUser(
            @PathVariable
            long id) {
        if (userService.getUserById(id).isPresent()) {
            userService.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
