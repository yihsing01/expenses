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

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    // READ current user progile
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(userMapper.toDto(currentUser));
    }

    // UPDATE current user progile
    @PutMapping("/me")
    public ResponseEntity<?> updateProfile(
            @Valid @RequestBody Map<String, String> updates,
            Authentication authentication
    ) {
        User currentUser = (User) authentication.getPrincipal();

        if (updates.containsKey("name")) {
            currentUser.setName(updates.get("name"));
        }

        if (updates.containsKey("email")) {
            String newEmail = updates.get("email").toLowerCase();
            if (userRepository.existsByEmail(newEmail) && !newEmail.equals(currentUser.getEmail().toLowerCase())) {
                return ResponseEntity.badRequest().body(Map.of("message", "Email already in use"));
            }
            currentUser.setEmail(newEmail);
        }

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

    // DELETE current user account
    @DeleteMapping("/me")
    public ResponseEntity<?> deleteOwnAccount(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        userRepository.delete(currentUser);
        return ResponseEntity.ok(Map.of("message", "Account deleted successfully"));
    }
}