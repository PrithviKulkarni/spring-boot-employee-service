package com.fdmgroup.employeeUI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class EmployeeUiPrithviKulkarniApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeUiPrithviKulkarniApplication.class, args);
	}

	@Bean
	WebClient.Builder builder() {
		return WebClient.builder();
	}
}
