package com.javaproject.expenses.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing expense categories.
 * Categories are used to classify transactions (e.g., Food, Transportation).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "Type is required")
    @Size(min = 2, max = 50, message = "Type must be between 2 and 50 characters")
    @Column(name = "type") // e.g., "income", "expense"
    private String type;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Transaction> transactions = new ArrayList<>();
}