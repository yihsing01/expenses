package com.javaproject.expenses.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CategoryDto {
    private Long id;
    private String name;
    private String type;
}