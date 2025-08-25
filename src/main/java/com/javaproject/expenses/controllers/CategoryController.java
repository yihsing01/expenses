package com.javaproject.expenses.controllers;

import com.javaproject.expenses.dtos.CategoryDto;
import com.javaproject.expenses.mappers.CategoryMapper;
import com.javaproject.expenses.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST Controller for managing category operations.
 * Provides read-only access to expense categories.
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    /**
     * Retrieves all available categories.
     *
     * @param authentication the current user's authentication context
     * @return list of all category DTOs
     */
    @GetMapping
    public List<CategoryDto> getAllCategories(Authentication authentication) {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    /**
     * Retrieves a specific category by ID.
     *
     * @param id the category ID
     * @param authentication the current user's authentication context
     * @return ResponseEntity containing the category DTO if found, or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id, Authentication authentication) {
        return categoryRepository.findById(id)
                .map(category -> ResponseEntity.ok(categoryMapper.toDto(category)))
                .orElse(ResponseEntity.notFound().build());
    }
}