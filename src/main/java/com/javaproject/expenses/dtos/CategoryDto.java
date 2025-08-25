package com.javaproject.expenses.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Data Transfer Object for Category entities.
 * Used for transferring category information between layers without exposing internal entity structure.
 */
@AllArgsConstructor
@Getter
public class CategoryDto {
    private Long id;
    private String name;
    private String type; // e.g., "income", "expense"
}