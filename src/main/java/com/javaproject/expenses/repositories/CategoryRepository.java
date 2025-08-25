package com.javaproject.expenses.repositories;

import com.javaproject.expenses.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for Category entity operations.
 * Provides standard CRUD operations through JpaRepository.
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
}