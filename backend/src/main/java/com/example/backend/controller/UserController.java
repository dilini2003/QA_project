package com.example.backend.controller;

import com.example.backend.model.User;
import com.example.backend.service.UserService;
import com.example.backend.util.JwtUtil;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import org.springframework.validation.BindingResult;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user ) {
        
        try {
            User saved = service.signup(user);
            return ResponseEntity.status(201).body( Map.of("message", "Signup Successful", "userId", saved.getId())
        );
            } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                Map.of("message", e.getMessage())
        );
        }
    }

    @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody User user, BindingResult result) {
    if (result.hasErrors()) {
        String errors = result.getAllErrors()
                .stream()
                .map(err -> err.getDefaultMessage())
                .reduce((msg1, msg2) -> msg1 + ", " + msg2)
                .orElse("Invalid input");
        return ResponseEntity.badRequest().body(errors);
    }
    try {
        User loggedIn = service.login(user.getEmail(), user.getPassword());
        String token = JwtUtil.generateToken(loggedIn.getUsername());
        return ResponseEntity.ok(
                Map.of("message", "Login Successful",  "token", token)
        );
        } catch (RuntimeException e) {
        return ResponseEntity.status(401).body(
                Map.of("message", "Invalid Credentials")
        );    }
}

}
