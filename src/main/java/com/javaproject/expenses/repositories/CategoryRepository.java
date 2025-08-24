package com.javaproject.expenses.repositories;

import com.javaproject.expenses.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
