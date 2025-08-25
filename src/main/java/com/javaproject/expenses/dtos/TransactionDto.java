package com.javaproject.expenses.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Transaction entities.
 * Contains transaction details without sensitive information or internal entity relationships.
 */
@AllArgsConstructor
@Getter
public class TransactionDto {
    private Long id;
    private Long userId;
    private Long categoryId;
    private BigDecimal amount; // positive for income, negative for expenses
    private String description;
    private LocalDateTime transactionDate;
    private LocalDateTime createdAt;
}