package com.example.demo_circuit_breaker.service;

import com.example.demo_circuit_breaker.reponse.Product;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class ProductServiceImp implements ProductService {
    private final WebClient webClient;
    private final ReactiveCircuitBreakerFactory<?, ?> circuitBreakerFactory;

    public ProductServiceImp(WebClient.Builder webClientBuilder,
                          ReactiveCircuitBreakerFactory<?, ?> circuitBreakerFactory) {
        this.webClient = webClientBuilder.baseUrl("https://dummyjson.com").build();
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    public Mono<Product> getProductById(Long id) {
        Mono<Product> remoteCall = webClient.get()
                .uri("/products/{id}", id)
                .retrieve()
                .bodyToMono(Product.class)
                // Convert 404 to RuntimeException
                .onErrorMap(WebClientResponseException.NotFound.class,
                        e -> new RuntimeException("Product not found", e));

        return circuitBreakerFactory.create("productService")
                .run(remoteCall, throwable -> fallbackProduct(id, throwable));
    }

    private Mono<Product> fallbackProduct(Long id, Throwable t) {
        System.out.println("Fallback triggered due to: " + t.getMessage());
        return Mono.just(new Product(id, "Fallback Product"));
    }
}
