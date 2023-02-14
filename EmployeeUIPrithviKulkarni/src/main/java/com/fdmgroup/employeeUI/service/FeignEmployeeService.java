package com.fdmgroup.employeeUI.service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import com.fdmgroup.employeeUI.exception.WebClientExceptionFilter;
import com.fdmgroup.employeeUI.model.Employee;

@FeignClient(name = "EMPLOYEE-API", path = "/api/employees")
public interface FeignEmployeeService {
	
	@GetMapping
	public List<Employee> getAllEmployees();

	@GetMapping("/{id}")
	public Employee getEmployeeById(@PathVariable(value="id") int id);

	@PutMapping("/{id}")
	public Employee updateEmployee(@RequestBody Employee employee, @PathVariable(value="id") int id);

	@PostMapping("/{id}")
	public Employee addEmployee(@RequestBody Employee employee);

	@DeleteMapping("/{id}")
	public void deleteEmployee(@PathVariable(value="id") int id);
}
