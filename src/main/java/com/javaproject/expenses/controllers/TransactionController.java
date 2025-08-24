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

@AllArgsConstructor
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionMapper transactionMapper;

    // ✅ FIXED: Only return current user's transactions
    @GetMapping
    public List<TransactionDto> getAllTransactions(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        return transactionRepository.findByUserId(currentUser.getId())
                .stream()
                .map(transactionMapper::toDto)
                .toList();
    }

    // ✅ FIXED: Only allow access to user's own transactions
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

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTransaction(
            @PathVariable Long id,
            @RequestBody Map<String, Object> payload,
            Authentication authentication
    ) {
        try {
            User currentUser = (User) authentication.getPrincipal();
            // ✅ FIXED: Use user-scoped query
            Optional<Transaction> optionalTransaction = transactionRepository.findByIdAndUserId(id, currentUser.getId());

            if (optionalTransaction.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Transaction not found"));
            }

            Transaction transaction = optionalTransaction.get();

            // Update fields
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

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTransaction(
            @PathVariable Long id,
            Authentication authentication
    ) {
        User currentUser = (User) authentication.getPrincipal();
        // ✅ FIXED: Use user-scoped query
        Optional<Transaction> optionalTransaction = transactionRepository.findByIdAndUserId(id, currentUser.getId());

        if (optionalTransaction.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Transaction not found");
        }

        transactionRepository.delete(optionalTransaction.get());
        return ResponseEntity.ok("Transaction deleted successfully");
    }
}