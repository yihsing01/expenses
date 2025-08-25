package com.javaproject.expenses.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Data Transfer Object for User entities.
 * Contains public user information without sensitive data like passwords.
 */
@AllArgsConstructor
@Getter
public class UserDto {
    private Long id;
    private String name;
    private String email;
}