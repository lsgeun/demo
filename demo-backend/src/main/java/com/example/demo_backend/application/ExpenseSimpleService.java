package com.example.demo_backend.application;

import com.example.demo_backend.domain.Expense;
import com.example.demo_backend.global.DomainException;
import com.example.demo_backend.domain.ExpenseMapper;
import com.example.demo_backend.infrastructure.ExpenseRepository;
import com.example.demo_backend.representation.ExpenseDto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.Value;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public ExpenseDto.ResponseDto increaseExpenseAmount(long id, int amount) {
        Expense expense = expenseRepository.findById(id).orElseThrow(() -> new DomainException("에러"));

        expense.increaseAmount(amount);

        return expenseMapper.toResponseDto(expense);
    }

    @Transactional
    public ExpenseDto.ResponseDto decreaseExpenseAmount(long id, int amount) {
        Expense expense = expenseRepository.findById(id).orElseThrow(() -> new DomainException("에러"));

        expense.decreaseAmount(amount);

        return expenseMapper.toResponseDto(expense);
    }

    @Transactional
    public ExpenseDto.ResponseDto updateExpenseAmount(long id, int amount) {
        Expense expense = expenseRepository.findById(id).orElseThrow(() -> new DomainException("에러"));

        expense.updateAmount(amount);

        return expenseMapper.toResponseDto(expense);
    }
}
