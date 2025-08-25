package com.javaproject.expenses.controllers;

import com.javaproject.expenses.models.User;
import com.javaproject.expenses.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST Controller for handling user authentication operations.
 * Manages user registration, login, logout, and current user retrieval.
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * Registers a new user account.
     * Validates email uniqueness and securely hashes the password before saving.
     *
     * @param user the user registration data
     * @return ResponseEntity containing success message with user details or error message
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email already exists"));
        }

        // Hash password before persisting to database
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        return ResponseEntity.ok(Map.of(
                "message", "Registration successful",
                "user", Map.of(
                        "id", savedUser.getId(),
                        "name", savedUser.getName(),
                        "email", savedUser.getEmail()
                )
        ));
    }

    /**
     * Authenticates an existing user with email and password.
     * Establishes a session-based authentication context on successful login.
     *
     * @param credentials map containing email and password
     * @param session HTTP session for storing authentication context
     * @return ResponseEntity containing success message with user details or error message
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials, HttpSession session) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.get("email"),
                            credentials.get("password")
                    )
            );

            // Persist authentication in security context and HTTP session
            SecurityContextHolder.getContext().setAuthentication(authentication);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    SecurityContextHolder.getContext());

            User user = (User) authentication.getPrincipal();
            return ResponseEntity.ok(Map.of(
                    "message", "Login successful",
                    "user", Map.of(
                            "id", user.getId(),
                            "name", user.getName(),
                            "email", user.getEmail()
                    )
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid email or password"));
        }
    }

    /**
     * Logs out the current authenticated user.
     * Invalidates the HTTP session and clears the security context.
     *
     * @param session HTTP session to invalidate
     * @return ResponseEntity with logout confirmation message
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(Map.of("message", "Logout successful"));
    }

    /**
     * Retrieves the current authenticated user's profile information.
     *
     * @return ResponseEntity containing user details if authenticated, or 401 error if not
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            User user = (User) auth.getPrincipal();
            return ResponseEntity.ok(Map.of(
                    "id", user.getId(),
                    "name", user.getName(),
                    "email", user.getEmail()
            ));
        }
        return ResponseEntity.status(401).body(Map.of("message", "Not authenticated"));
    }
}