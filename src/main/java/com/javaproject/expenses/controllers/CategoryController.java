package com.javaproject.expenses.controllers;

import com.javaproject.expenses.dtos.CategoryDto;
import com.javaproject.expenses.mappers.CategoryMapper;
import com.javaproject.expenses.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    // READ categories
    @GetMapping
    public List<CategoryDto> getAllCategories(Authentication authentication) {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    // READ category by ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id, Authentication authentication) {
        return categoryRepository.findById(id)
                .map(category -> ResponseEntity.ok(categoryMapper.toDto(category)))
                .orElse(ResponseEntity.notFound().build());
    }
}