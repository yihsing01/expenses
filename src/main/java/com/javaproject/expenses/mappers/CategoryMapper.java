package com.javaproject.expenses.mappers;

import com.javaproject.expenses.dtos.CategoryDto;
import com.javaproject.expenses.models.Category;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper for converting between Category entity and CategoryDto.
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto toDto(Category category);
}
