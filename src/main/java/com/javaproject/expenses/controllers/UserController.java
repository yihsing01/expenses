package com.javaproject.expenses.controllers;

import com.javaproject.expenses.mappers.UserMapper;
import com.javaproject.expenses.models.User;
import com.javaproject.expenses.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * REST Controller for managing user profile operations.
 * Handles user profile retrieval, updates, and account deletion.
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Retrieves the current authenticated user's profile information.
     *
     * @param authentication the current user's authentication context
     * @return ResponseEntity containing the user's profile DTO
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(userMapper.toDto(currentUser));
    }

    /**
     * Updates the current authenticated user's profile information.
     * Supports partial updates for name, email, and password fields.
     *
     * @param updates map containing fields to update (name, email, password)
     * @param authentication the current user's authentication context
     * @return ResponseEntity containing success message with updated user details or error message
     */
    @PutMapping("/me")
    public ResponseEntity<?> updateProfile(
            @Valid @RequestBody Map<String, String> updates,
            Authentication authentication
    ) {
        User currentUser = (User) authentication.getPrincipal();

        // Update name if provided
        if (updates.containsKey("name")) {
            currentUser.setName(updates.get("name"));
        }

        // Update email with uniqueness validation
        if (updates.containsKey("email")) {
            String newEmail = updates.get("email").toLowerCase();
            if (userRepository.existsByEmail(newEmail) && !newEmail.equals(currentUser.getEmail().toLowerCase())) {
                return ResponseEntity.badRequest().body(Map.of("message", "Email already in use"));
            }
            currentUser.setEmail(newEmail);
        }

        // Update password with secure hashing
        if (updates.containsKey("password")) {
            String newPassword = updates.get("password");
            currentUser.setPassword(passwordEncoder.encode(newPassword));
        }

        userRepository.save(currentUser);

        return ResponseEntity.ok(Map.of(
                "message", "Profile updated successfully",
                "user", Map.of(
                        "id", currentUser.getId(),
                        "name", currentUser.getName(),
                        "email", currentUser.getEmail()
                )
        ));
    }

    /**
     * Deletes the current authenticated user's account permanently.
     *
     * @param authentication the current user's authentication context
     * @return ResponseEntity with account deletion confirmation message
     */
    @DeleteMapping("/me")
    public ResponseEntity<?> deleteOwnAccount(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        userRepository.delete(currentUser);
        return ResponseEntity.ok(Map.of("message", "Account deleted successfully"));
    }
}