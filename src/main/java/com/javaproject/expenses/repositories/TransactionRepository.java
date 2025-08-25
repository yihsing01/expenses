package com.javaproject.expenses.repositories;

import com.javaproject.expenses.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Transaction entity operations.
 * Provides transaction queries with user-based filtering for security.
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Finds all transactions belonging to a specific user.
     */
    List<Transaction> findByUserId(Long userId);

    /**
     * Finds a transaction by ID that belongs to a specific user.
     * Used for security to ensure users can only access their own transactions.
     */
    Optional<Transaction> findByIdAndUserId(Long id, Long userId);
}