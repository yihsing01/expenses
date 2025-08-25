package com.javaproject.expenses.controllers;

import com.javaproject.expenses.dtos.TransactionDto;
import com.javaproject.expenses.mappers.TransactionMapper;
import com.javaproject.expenses.models.Category;
import com.javaproject.expenses.models.Transaction;
import com.javaproject.expenses.models.User;
import com.javaproject.expenses.repositories.TransactionRepository;
import com.javaproject.expenses.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST Controller for managing transaction operations.
 * Handles CRUD operations for transactions with user authentication.
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionMapper transactionMapper;

    /**
     * Retrieves all transactions for the authenticated user.
     *
     * @param authentication the current user's authentication context
     * @return list of transaction DTOs belonging to the authenticated user
     */
    @GetMapping
    public List<TransactionDto> getAllTransactions(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        return transactionRepository.findByUserId(currentUser.getId())
                .stream()
                .map(transactionMapper::toDto)
                .toList();
    }

    /**
     * Retrieves a specific transaction by ID for the authenticated user.
     *
     * @param id the transaction ID
     * @param authentication the current user's authentication context
     * @return ResponseEntity containing the transaction DTO or error message
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getTransactionById(@PathVariable Long id, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        Optional<Transaction> optional = transactionRepository.findByIdAndUserId(id, currentUser.getId());

        if (optional.isPresent()) {
            return ResponseEntity.ok(transactionMapper.toDto(optional.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Transaction not found"));
        }
    }

    /**
     * Creates a new transaction for the authenticated user.
     *
     * @param payload the transaction data containing categoryId, amount, and description
     * @param authentication the current user's authentication context
     * @return ResponseEntity containing the created transaction DTO or error message
     */
    @PostMapping
    public ResponseEntity<?> createTransaction(
            @RequestBody Map<String, Object> payload,
            Authentication authentication
    ) {
        try {
            User currentUser = (User) authentication.getPrincipal();
            Long categoryId = Long.valueOf(payload.get("categoryId").toString());
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid category"));

            Transaction transaction = new Transaction();
            transaction.setUser(currentUser);
            transaction.setCategory(category);
            transaction.setAmount(new BigDecimal(payload.get("amount").toString()));
            transaction.setDescription(payload.get("description").toString());

            Transaction saved = transactionRepository.save(transaction);
            return ResponseEntity.status(HttpStatus.CREATED).body(transactionMapper.toDto(saved));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    /**
     * Updates an existing transaction for the authenticated user.
     * Only updates fields that are present in the payload.
     *
     * @param id the transaction ID to update
     * @param payload the transaction data to update (can contain categoryId, amount, description, transactionDate)
     * @param authentication the current user's authentication context
     * @return ResponseEntity containing the updated transaction DTO or error message
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTransaction(
            @PathVariable Long id,
            @RequestBody Map<String, Object> payload,
            Authentication authentication
    ) {
        try {
            User currentUser = (User) authentication.getPrincipal();
            Optional<Transaction> optionalTransaction = transactionRepository.findByIdAndUserId(id, currentUser.getId());

            if (optionalTransaction.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Transaction not found"));
            }

            Transaction transaction = optionalTransaction.get();

            // Update fields conditionally based on payload content
            if (payload.containsKey("categoryId")) {
                Long categoryId = Long.valueOf(payload.get("categoryId").toString());
                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid category"));
                transaction.setCategory(category);
            }

            if (payload.containsKey("amount")) {
                transaction.setAmount(new BigDecimal(payload.get("amount").toString()));
            }

            if (payload.containsKey("description")) {
                transaction.setDescription(payload.get("description").toString());
            }

            if (payload.containsKey("transactionDate")) {
                transaction.setTransactionDate(LocalDateTime.parse(payload.get("transactionDate").toString()));
            }

            Transaction updated = transactionRepository.save(transaction);
            return ResponseEntity.ok(transactionMapper.toDto(updated));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    /**
     * Deletes a transaction for the authenticated user.
     *
     * @param id the transaction ID to delete
     * @param authentication the current user's authentication context
     * @return ResponseEntity with success message or error message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTransaction(
            @PathVariable Long id,
            Authentication authentication
    ) {
        User currentUser = (User) authentication.getPrincipal();
        Optional<Transaction> optionalTransaction = transactionRepository.findByIdAndUserId(id, currentUser.getId());

        if (optionalTransaction.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Transaction not found");
        }

        transactionRepository.delete(optionalTransaction.get());
        return ResponseEntity.ok("Transaction deleted successfully");
    }
}