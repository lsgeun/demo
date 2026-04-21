package com.example.demo_backend;

import com.example.demo_backend.domain.Expense;
import com.example.demo_backend.infrastructure.ExpenseRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SimpleTest {

    private final ExpenseRepository expenseRepository;
    private final WebClient webClient;

    @LocalServerPort // 2. 위에서 설정한 랜덤 포트 번호를 자동으로 가져옵니다.
    private int port;

    @Autowired
    public SimpleTest(ExpenseRepository expenseRepository, WebClient webClient) {
        this.expenseRepository = expenseRepository;
        this.webClient = webClient;
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

    @Test
    public void loadTest() throws InterruptedException {
        // 총 요청 횟수
        int totalRequests = 1000;
        // 모든 비동기 작업이 끝날 때까지 대기하기 위한 장치
        CountDownLatch latch = new CountDownLatch(totalRequests);

        long startTime = System.currentTimeMillis();

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger errorCount = new AtomicInteger(0);

        for (int i = 0; i < totalRequests; i++) {
            int id = (i % 5) + 1;

            webClient.get()
                    .uri("http://localhost:" + port + "/expense/{id}", id)
                    .exchangeToMono(response -> {
                        if (response.statusCode().is2xxSuccessful()) {
                            successCount.incrementAndGet();
                        } else {
                            errorCount.incrementAndGet();
                        }
                        return response.bodyToMono(String.class);
                    })
                    .subscribe(
                        result -> {
                            // 성공 시 처리
                            latch.countDown();
                        },
                        error -> {
                            System.err.println("에러 발생: " + error.getMessage());
                            latch.countDown();
                        }
                    );
        }

        // 모든 요청이 끝날 때까지 최대 1분간 대기
        latch.await(1, TimeUnit.MINUTES);

        long endTime = System.currentTimeMillis();
        System.out.println("총 소요 시간: " + (endTime - startTime) + "ms");

        System.out.println("성공: " + successCount.get() + ", 실패: " + errorCount.get());
        assertEquals(totalRequests, successCount.get(), "모든 요청이 성공해야 합니다.");
    }
}
