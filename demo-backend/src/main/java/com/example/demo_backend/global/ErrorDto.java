package com.example.demo_backend.global;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ErrorDto {
    String message;
}
