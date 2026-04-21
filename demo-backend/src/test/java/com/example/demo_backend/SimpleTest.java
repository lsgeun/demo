package com.example.demo_backend;

import com.example.demo_backend.domain.Expense;
import com.example.demo_backend.infrastructure.ExpenseRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class SimpleTest {

    private final ExpenseRepository expenseRepository;

    @Autowired
    public SimpleTest(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Test
    public void insertData() {
        for (long i = 0; i < 100; i++) {
            expenseRepository.save(new Expense(null, (int) i, new Date()));
        }

        printRepository();
    }

    @Test
    public void showDate() {

        printRepository();
    }


    private void printRepository() {
        System.out.println("---------------출력---------------");
        for (Expense expense : expenseRepository.findAll()) {
            System.out.println(expense.getId() + ", " + expense.getAmount() + ", " + expense.getDate());
        }
        System.out.println("--------------------------------");
    }
}
