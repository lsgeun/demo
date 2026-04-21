package com.example.demo_backend.representation;

import com.example.demo_backend.application.ExpenseSimpleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseSimpleService expenseSimpleService;

    @RequestMapping (method = RequestMethod.GET, value = "/expense/{id}")
    public ResponseEntity<ExpenseDto.ResponseDto> readExpense(@PathVariable Long id) {
        ExpenseDto.ResponseDto responseDto = expenseSimpleService.readExpense(id);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
