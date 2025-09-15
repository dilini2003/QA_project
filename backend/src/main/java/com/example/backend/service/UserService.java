package com.example.backend.service;

import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {
    private final UserRepository repo;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    private static final Pattern EMAIL_REGEX = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    private boolean validateEmail(String email) {
        return email != null && EMAIL_REGEX.matcher(email).matches();
    }

    public User signup(User user) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty() ||
                user.getEmail() == null || user.getEmail().trim().isEmpty() ||
                user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new RuntimeException("Name, Email and Password are required");
        }
        
        // Validate email format
        if (!validateEmail(user.getEmail())) {
            throw new RuntimeException("Invalid email format");
        }

        // (OPTIONAL but recommended) Password strength validation
        if (user.getPassword().length() < 8) {
            throw new RuntimeException("Password must be at least 8 characters");
        }

         // Hash password before storing
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Prevent duplicate email
        if (repo.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered!");
        }
        return repo.save(user);
    }

    public User login(String email, String password) {
        Optional<User> user = repo.findByEmail(email);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return user.get();
        }
        throw new RuntimeException("Invalid credentials");
    }

    public User findById(String id) {
        Optional<User> user = repo.findById(id);
        return user.orElseThrow(() -> new RuntimeException("User not found"));
    }

}
