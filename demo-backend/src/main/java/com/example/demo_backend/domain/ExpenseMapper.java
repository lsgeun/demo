package com.example.demo_backend.domain;

import com.example.demo_backend.representation.ExpenseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {

    Expense toExpense(ExpenseDto.RequestDto readRequestDto);
    ExpenseDto.ResponseDto toResponseDto(Expense expense);
}
