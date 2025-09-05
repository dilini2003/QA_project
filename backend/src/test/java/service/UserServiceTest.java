package service;

import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository repo;
    private UserService service;

    @BeforeEach
    void setup() {
        repo = mock(UserRepository.class);
        service = new UserService(repo);
    }

    @Test
    void testSignupSuccess() {
        User user = new User();
        user.setUsername("dilini");
        user.setEmail("dilini@example.com");
        user.setPassword("12345");

        when(repo.findByEmail("dilini@example.com")).thenReturn(Optional.empty());
        when(repo.save(user)).thenReturn(user);

        User result = service.signup(user);

        assertEquals("dilini", result.getUsername());
        assertEquals("dilini@example.com", result.getEmail());
    }

    @Test
    void testSignupFailWhenEmailExists() {
        User user = new User();
        user.setEmail("dilini@example.com");

        when(repo.findByEmail("dilini@example.com")).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () -> service.signup(user));
    }

    @Test
    void testLoginSuccess() {
        User user = new User();
        user.setEmail("dilini@example.com");
        user.setPassword("12345");

        when(repo.findByEmail("dilini@example.com")).thenReturn(Optional.of(user));

        User result = service.login("dilini@example.com", "12345");

        assertEquals("dilini@example.com", result.getEmail());
    }

    @Test
    void testLoginFailInvalidPassword() {
        User user = new User();
        user.setEmail("dilini@example.com");
        user.setPassword("12345");

        when(repo.findByEmail("dilini@example.com")).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () -> service.login("dilini@example.com", "wrongpass"));
    }
}
