package com.javaproject.expenses.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class TransactionDto {
    private Long id;
    private Long userId;
    private Long categoryId;
    private BigDecimal amount;
    private String description;
    private LocalDateTime transactionDate;
    private LocalDateTime createdAt;
}
