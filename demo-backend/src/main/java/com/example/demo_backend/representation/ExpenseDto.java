package com.example.demo_backend.representation;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import java.util.Date;


public class ExpenseDto {
    @Value
    public static class ReadRequestDto {
        private Long id;
        private Integer amount;
        private Date date;
    }

    @Getter
    @Builder
    public static class ResponseDto {
        private Long id;
        private Integer amount;
        private Date date;
    }
}
