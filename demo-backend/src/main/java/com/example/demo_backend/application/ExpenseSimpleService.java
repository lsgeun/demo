package com.example.demo_backend.application;

import com.example.demo_backend.domain.Expense;
import com.example.demo_backend.global.DomainException;
import com.example.demo_backend.domain.ExpenseMapper;
import com.example.demo_backend.infrastructure.ExpenseRepository;
import com.example.demo_backend.representation.ExpenseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
// 생성자 의존성 주입은 @RequiredArgsConstructor로 대체 가능
@RequiredArgsConstructor
public class ExpenseSimpleService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseMapper expenseMapper;

    public ExpenseDto.ResponseDto readExpense(Long id) {
        Expense expense = expenseRepository.findById(id).orElseThrow(() -> new DomainException("에러"));

        return expenseMapper.toResponseDto(expense);
    }
}
