package fr.negosud.springapi.api.controller;

import fr.negosud.springapi.api.model.dto.SetUserRequest;
import fr.negosud.springapi.api.model.entity.User;
import fr.negosud.springapi.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/user")
@Tag(name = "User", description = "Endpoints related to User crud and actions.")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @ApiResponse(description = "List of all users", responseCode = "200")
    public ResponseEntity<List<User>> getAllUsers(
            @RequestParam(required = false)
            String userGroupName,
            @RequestParam(required = false, defaultValue = "true")
            boolean active) {
        List<User> users = userService.getAllUsers(active, userGroupName);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(description = "User found", responseCode = "200"),
            @ApiResponse(description = "User not found", responseCode = "404", content = {
                    @Content(schema = @Schema())
            })
    })
    public ResponseEntity<User> getUserById(
            @PathVariable
            long id) {
        return userService.getUserById(id)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ApiResponse(description = "User created", responseCode = "201")
    public ResponseEntity<User> createUser(
            @RequestBody
            SetUserRequest createUserRequest) {
        User user = userService.setUserFromRequest(createUserRequest, null);
        user = userService.saveUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(description = "Update a user by ID.")
    @ApiResponses(value = {
            @ApiResponse(description = "User updated successfully", responseCode = "200"),
            @ApiResponse(description = "User not found", responseCode = "404", content = {
                    @Content(schema = @Schema())
            })
    })
    public ResponseEntity<User> updateUser(
            @PathVariable
            long id,
            @RequestBody
            SetUserRequest updateUserRequest) {
        User user = userService.getUserById(id).orElse(null);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            user = userService.setUserFromRequest(updateUserRequest, user);
            user = userService.saveUser(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(description = "User deleted", responseCode = "204"),
            @ApiResponse(description = "User not found", responseCode = "404")
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
