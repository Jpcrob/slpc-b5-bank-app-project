package com.webapi.controller;

import com.webapi.entity.Balance;
import com.webapi.entity.User;
import com.webapi.repository.BalanceRepository;
import com.webapi.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@RestController
@RequestMapping(path = "/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(path = "/add")
    public @ResponseBody String addNewUser(@RequestParam String name
            , @RequestParam String email , @RequestParam String password) {

        String hashedPassword = passwordEncoder.encode(password);

        User n = new User();
        n.setName(name);
        n.setEmail(email);
        n.setPassword(hashedPassword);
        userRepository.save(n);

        Balance bal = new Balance();
        bal.setAmount(1000);
        bal.setUser(n);
        balanceRepository.save(bal);

        return "Saved";
    }

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<User> getTutorialById(@PathVariable("id") int id) {
        Optional<User> userData = userRepository.findById(id);

        if (userData.isPresent()) {
            return new ResponseEntity<>(userData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/user/{username}/{password}")
    public @ResponseBody Iterable<User> getUserByCredentials(
            @PathVariable String username,
            @PathVariable String password) {

        return userRepository.findByUsernameAndPassword(username, password);
    }
}