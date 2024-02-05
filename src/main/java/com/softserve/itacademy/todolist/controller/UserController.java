package com.softserve.itacademy.todolist.controller;

import com.softserve.itacademy.todolist.dto.UserRequest;
import com.softserve.itacademy.todolist.dto.UserResponse;
import com.softserve.itacademy.todolist.model.User;
import com.softserve.itacademy.todolist.service.RoleService;
import com.softserve.itacademy.todolist.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService ) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getUsers() {
        log.info("Get all users");
        List<UserResponse> userResponses = userService.getAll().stream()
                .map(UserResponse::new)
                .toList();
        return new ResponseEntity<>(userResponses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        log.info("Get user by id: " + id);
        User user = userService.readById(id);
        if (user != null) {
            UserResponse userResponse = new UserResponse(user);
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        log.info("Create User");
        User newUser = new User();
        newUser.setFirstName(userRequest.getFirstName());
        newUser.setLastName(userRequest.getLastName());
        newUser.setEmail(userRequest.getEmail());
        newUser.setPassword(userRequest.getPassword());
        newUser.setRole(roleService.readById(2));
        User createdUser = userService.create(newUser);
        UserResponse userResponse = new UserResponse(createdUser);
        log.info("User successfully created");
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest userRequest) {
        log.info("Update User");
        User existingUser = userService.readById(id);
        if (existingUser != null) {
            existingUser.setFirstName(userRequest.getFirstName());
            existingUser.setLastName(userRequest.getLastName());
            existingUser.setEmail(userRequest.getEmail());
            existingUser.setRole(userRequest.getRole());
            User updatedUser = userService.update(existingUser);
            UserResponse userResponse = new UserResponse(updatedUser);
            log.info("User successfully updated");
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("Delete User");
        User user = userService.readById(id);
        if (user != null) {
            userService.delete(id);
            log.info("User successfully deleted");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
