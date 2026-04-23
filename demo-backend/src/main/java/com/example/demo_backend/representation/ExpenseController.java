package com.example.demo_backend.representation;

import com.example.demo_backend.application.ExpenseSimpleService;
import com.example.demo_backend.domain.Expense;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseSimpleService expenseSimpleService;

    @RequestMapping (method = RequestMethod.GET, value = "/expense/{id}")
    public ResponseEntity<ExpenseDto.ResponseDto> readExpense(@PathVariable Long id) {
        ExpenseDto.ResponseDto responseDto = expenseSimpleService.readExpense(id);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/expense/{id}/increaseAmount")
    public ResponseEntity<ExpenseDto.ResponseDto> increaseExpenseAmount(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") int amount
    ) {
        ExpenseDto.ResponseDto responseDto = expenseSimpleService.increaseExpenseAmount(id, amount);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/expense/{id}/decreaseAmount")
    public ResponseEntity<ExpenseDto.ResponseDto> decreaseExpenseAmount(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") int amount
    ) {
        ExpenseDto.ResponseDto responseDto = expenseSimpleService.decreaseExpenseAmount(id, amount);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
