package com.example.demo_circuit_breaker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class DemoCircuitBreakerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoCircuitBreakerApplication.class, args);
	}

}
