package com.example.demo_backend.domain;

import com.example.demo_backend.global.DomainException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Expense {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer amount;
    private Date date;

    @Version  // 낙관적 잠금을 위한 버전 필드 추가
    @Column(nullable = false)
    private Long version = 0L;

    public void decreaseAmount(int amount) {
        if (this.amount - amount < 0) {
            throw new DomainException("비용량 감소 에러");
        }

        this.amount -= amount;
    }

    public void increaseAmount(int amount) {
        if (this.amount + amount > 9999) {
            throw new DomainException("비용량 증가 에러");
        }

        this.amount += amount;
    }

    public void updateAmount(int amount) {
        if (!(0 <= amount && amount <= 9999)) {
            throw new DomainException("비용량 업데이트 에러");
        }

        this.amount = amount;
    }
}
