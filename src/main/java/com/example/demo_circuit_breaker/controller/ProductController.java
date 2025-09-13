package com.example.demo_circuit_breaker.controller;

import com.example.demo_circuit_breaker.reponse.Product;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final WebClient client;

    @GetMapping("/{id}")
    @Bulkhead(name = "productService", fallbackMethod = "fallbackProduct", type = Bulkhead.Type.SEMAPHORE)
//    @CircuitBreaker(name = "productService", fallbackMethod = "fallbackProduct")
//    @Retry(name = "productService", fallbackMethod = "fallbackRetry")
//    @TimeLimiter(name = "productService")
//    @Retry(name = "productService")
//    @RateLimiter(name = "productService")
    public Mono<Product> getProductById(@PathVariable Long id) {
        return client.get()
                        .uri("https://dummyjson.com/products/{id}", id)
                        .retrieve()
                        .bodyToMono(Product.class)
                        .onErrorMap(WebClientResponseException.NotFound.class,
                                e -> new RuntimeException("Product not found", e));
    }

    @Retry(name = "productService", fallbackMethod = "fallbackRetryString")
    @GetMapping("/users")
    public Mono<String> callUserService() {
        return client
                .get()
                .uri("http://localhost:8081/users") // Eureka service name
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<Product> fallbackProduct(Long id, Throwable t) {
        System.out.println("Fallback: " + t.getMessage());
        return Mono.just(new Product(id, "No Product1"));
    }

    public Mono<Product> fallbackRetry(Long id, Throwable t) {
        System.out.println("Fallback called due to: {}" + t.getMessage());
        return Mono.just(new Product(id, "No Product22"));
    }

    public Mono<String> fallbackRetryString(Throwable t) {
        System.out.println("Fallback called due to: {}" + t.getMessage());
        return Mono.just("Failed to fetch");
    }

//    private final RestTemplate restTemplate = new RestTemplate();
//
//    @GetMapping("/{id}")
//    @CircuitBreaker(name = "productService", fallbackMethod = "fallbackProduct")
//    public Product getProductById(Long id) {
//        return restTemplate.getForObject("https://dummyjson.com/products/{id}", Product.class, id);
//    }
//
//    // Fallback method
//    public Product fallbackProduct(Long id, Throwable t) {
//        System.out.println("Fallback triggered: " + t.getMessage());
//        return new Product(id, "Fallback Product");
//    }


//    private final ProductService productService;

//    @Retry(name = "productService")
//    @RateLimiter(name = "productService")
//    @Bulkhead(name = "productService")
//    @GetMapping("/{id}")
//    @CircuitBreaker(name = "productService", fallbackMethod = "fallbackProduct")
//    public Mono<Product> getProductById(@PathVariable Long id) {
//        return productService.getProductById(id);
//    }
}
