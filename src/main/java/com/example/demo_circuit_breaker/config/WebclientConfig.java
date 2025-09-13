package com.example.demo_circuit_breaker.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebclientConfig {
    @Bean
    @LoadBalanced
    public WebClient webClient(WebClient.Builder builder) {
        return builder
                .defaultHeader("Accept", "application/json")
                .filter((request, next) -> {
                    System.out.println("Calling: " + request.url());
                    return next.exchange(request);
                })
                .build();
    }
}
