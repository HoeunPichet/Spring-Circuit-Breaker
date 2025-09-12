package com.example.demo_circuit_breaker.service;

import com.example.demo_circuit_breaker.reponse.Product;
import reactor.core.publisher.Mono;

public interface ProductService {
    Mono<Product> getProductById(Long id);
}
