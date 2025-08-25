package com.javaproject.expenses.repositories;

import com.javaproject.expenses.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repository interface for User entity operations.
 * Provides user authentication and registration support methods.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their email address.
     * Used for authentication and login processes.
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if a user exists with the given email address.
     * Used for registration validation to prevent duplicate emails.
     */
    boolean existsByEmail(String email);
}