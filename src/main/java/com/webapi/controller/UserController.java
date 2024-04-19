package com.webapi.controller;

import com.webapi.entity.User;
import com.webapi.repository.BalanceRepository;
import com.webapi.repository.UserRepository;
import com.webapi.DTO.LoginRequest;

import com.webapi.services.UserService;
import com.webapi.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BalanceRepository balanceRepository;

    @PostMapping("/add")
    public ResponseEntity<GenericResponse<User>> addNewUser(@RequestBody(required = false) User newUser) {

        String username = newUser.getUsername();
        String email = newUser.getEmail();
        String mobile = newUser.getMobile();

        if (!userService.findByUsername(username).isPresent() &&
                !userService.findByEmail(email).isPresent() &&
                !userService.findByMobile(mobile).isPresent()) {

            userService.createUserWithInitialBalance(newUser, 1000);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(GenericResponse.<User>success(newUser, "User created successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(GenericResponse.<User>error("User already exists or invalid input"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<GenericResponse<String>> loginUser(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.username();
        String password = loginRequest.password();

        if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
            Optional<User> userOptional = userRepository.findByUsernameAndPassword(username, password);

            if (userOptional.isPresent()) {
                return ResponseEntity.ok(GenericResponse.<String>success("Login successful", "User authenticated"));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(GenericResponse.<String>error("Invalid username or password"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(GenericResponse.<String>error("Invalid input"));
        }
    }

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<GenericResponse<User>> getTutorialById(@PathVariable("id") int id) {

        Optional<User> userData = userRepository.findById(id);

        if (userData.isPresent()) {
            return ResponseEntity.ok(GenericResponse.<User>success(userData.get(), "User found"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(GenericResponse.<User>error("User not found"));
        }
    }
    @GetMapping(path = "/username/{username}")
    public ResponseEntity<GenericResponse<User>> getUserByUsername(@PathVariable("username") String username) {

        Optional<User> user = Optional.ofNullable(userRepository.findByUsername(username));

        if (user.isPresent()) {
            return ResponseEntity.ok(GenericResponse.<User>success(user.get(), "User found"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(GenericResponse.<User>error("User not found"));
        }
    }
}