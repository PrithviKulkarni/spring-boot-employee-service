package com.fdmgroup.employeeUI.service;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import com.fdmgroup.employeeUI.exception.WebClientExceptionFilter;
import com.fdmgroup.employeeUI.model.Employee;

@Service
public class WebClientEmployeeService {
	
	private final WebClient webClient;
	private final String REST_API_URL = "http://localhost:8088/api/employees/";
	private ExchangeFilterFunction responseFilter =
			ExchangeFilterFunction.ofResponseProcessor(WebClientExceptionFilter::filterResponseFunction);

	public WebClientEmployeeService(WebClient.Builder builder) {
		super();
		this.webClient = builder
				.baseUrl(REST_API_URL)
				.filter(responseFilter)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.build();
	}
	
	public List<Employee> getAllEmployees() {
		return webClient.get()
				.retrieve()
				.bodyToFlux(Employee.class) 
				.collectList()
				.block();
	}

	public Employee getEmployeeById(int id) {
		return webClient.get()
				.uri(builder -> builder.path("{id}").build(id))
				.retrieve()
				.bodyToMono(Employee.class)
				.block();
	}

	public Employee updateEmployee(Employee employee, int id) {
		return webClient.put()
				.uri(builder -> builder.path("{id}").build(id))
				.bodyValue(employee)
				.retrieve()
				.bodyToMono(Employee.class)
				.block();
	}

	public Employee addEmployee(Employee employee) {
		return webClient.post()
				.bodyValue(employee)
				.retrieve()
				.bodyToMono(Employee.class)
				.block();
	}
	
	public void deleteEmployee(int id) {
		webClient.delete()
			.uri(builder -> builder.path("{id}").build(id))
			.retrieve()
			.toBodilessEntity()
			.block();
	}	
}
