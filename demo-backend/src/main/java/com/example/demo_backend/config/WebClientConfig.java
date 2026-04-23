package com.example.demo_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        // ConnectionProvider 설정
        ConnectionProvider provider = ConnectionProvider.builder("my-custom-provider")
                .maxConnections(500)         // (1) 최대 동시 연결 수 (테이블 수)
                .pendingAcquireMaxCount(-1) // (2) 연결이 꽉 찼을 때 기다릴 요청 수 (대기석)
                .pendingAcquireTimeout(Duration.ofSeconds(45)) // 기다리는 최대 시간
                .build();

        // 엔진(HttpClient)에 ConnectionProvider 등록
        HttpClient httpClient = HttpClient.create(provider);

        return WebClient.builder()
                .baseUrl("http://localhost:8080")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
