package com.fdmgroup.employeeServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EmployeeServerPrithviKulkarniApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeServerPrithviKulkarniApplication.class, args);
	}

}
