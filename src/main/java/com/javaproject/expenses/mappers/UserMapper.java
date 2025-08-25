package com.javaproject.expenses.mappers;

import com.javaproject.expenses.dtos.UserDto;
import com.javaproject.expenses.models.User;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper for converting between User entity and UserDto.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
}