package com.example.demo_backend;

import com.example.demo_backend.application.ExpenseSimpleService;
import com.example.demo_backend.representation.ExpenseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SimpleTest {

    private final ExpenseSimpleService expenseSimpleService;
    private final WebClient webClient;

    @LocalServerPort // 2. 위에서 설정한 랜덤 포트 번호를 자동으로 가져옵니다.
    private int port;

    @Autowired
    public SimpleTest(ExpenseSimpleService expenseSimpleService, WebClient webClient) {
        this.expenseSimpleService = expenseSimpleService;
        this.webClient = webClient;
    }

    @Test
    public void loadTest() throws InterruptedException {
        int id = 1;
        // totalAmount == requestAmount * totalRequests 이어야 테스트 통과 가능성이 있음.
        // 그리고 동시성 문제를 해결해서 감소된 amount가 0이 되어야 테스트 통과임.
        int totalAmount = 1000;
        int requestAmount = 1;
        int totalRequests = 1000;
        AtomicBoolean existsError = new AtomicBoolean(false);

        // 데이터를 지정된 값으로 초기화
        expenseSimpleService.updateExpenseAmount(id, totalAmount);
        // 모든 비동기 작업이 끝날 때까지 대기하기 위한 장치
        CountDownLatch latch = new CountDownLatch(totalRequests);

        for (int i = 0; i < totalRequests; i++) {
            webClient.patch()
                    .uri("http://localhost:" + port + "/expense/{id}/decreaseAmount?amount=" + requestAmount, id)
                    .retrieve()
                    .bodyToMono(String.class) // 응답 바디(JSON 등)를 문자열로 받음
                    // 29초
                    // .retryWhen(Retry.max(100) // 최대 100번까지 다시 시도
                    //         .filter(throwable -> throwable.getMessage().contains("500")) // 500 에러일 때 재시도
                    // )
                    // 37초
                    .retryWhen(Retry.backoff(100, Duration.ofMillis(10)) // 최대 100번, 시작은 10ms 대기
                            .jitter(0.75) // 대기 시간에 75% 정도의 무작위성 추가 (Jitter)
                            .filter(throwable -> throwable instanceof WebClientResponseException &&
                                    ((WebClientResponseException) throwable).getStatusCode().is5xxServerError())
                    )
                    .doOnNext(result -> {
                        System.out.println(result); // 실행 시점의 amount 변화 출력
                    })
                    .doOnError(error -> {
                        System.err.println("에러 발생: " + error.getMessage());

                        existsError.set(true);
                    })
                    .subscribe(
                            res -> latch.countDown(),
                            err -> latch.countDown()
                    );
        }

        // 모든 요청이 끝날 때까지 최대 1분간 대기
        latch.await(1, TimeUnit.MINUTES);

        // 최종 결과 확인을 위한 GET 요청
        Integer finalAmount = webClient.get()
                .uri("http://localhost:" + port + "/expense/{id}", id)
                .retrieve()
                .bodyToMono(ExpenseDto.ResponseDto.class) // 실제 사용하는 DTO 클래스명으로 변경하세요
                .map(ExpenseDto.ResponseDto::getAmount)
                .block(); // 테스트 코드이므로 결과를 기다리기 위해 block() 사용

        // 검증
        System.out.println("최종 잔액: " + finalAmount);
        assertEquals(0, finalAmount, "동시성 이슈로 인해 잔액이 0이 아닙니다.");
        assertFalse(existsError.get(), "테스트 진행중 에러가 발생");
    }
}
