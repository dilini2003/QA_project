package com.example.backend.service;

import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {
    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    private boolean validateEmail(String email) {
        return email != null && EMAIL_REGEX.matcher(email).matches();
    }

    public User signup(User user) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty() ||
        user.getEmail() == null || user.getEmail().trim().isEmpty() ||
        user.getPassword() == null || user.getPassword().trim().isEmpty()) {
        throw new RuntimeException("Name, Email and Password are required");
    }

    if (!validateEmail(user.getEmail())) {
            throw new RuntimeException("Invalid email format");
        }
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

}
