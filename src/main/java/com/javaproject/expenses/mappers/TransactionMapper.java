package com.javaproject.expenses.mappers;

import com.javaproject.expenses.dtos.TransactionDto;
import com.javaproject.expenses.models.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for converting between Transaction entity and TransactionDto.
 */
@Mapper(componentModel = "spring")
public interface TransactionMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "category.id", target = "categoryId")
    TransactionDto toDto(Transaction transaction);
}