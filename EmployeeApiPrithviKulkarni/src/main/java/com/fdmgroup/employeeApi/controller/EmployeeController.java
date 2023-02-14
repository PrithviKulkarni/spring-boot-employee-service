package com.fdmgroup.employeeApi.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import com.fdmgroup.employeeApi.exception.EmployeeExistsException;
import com.fdmgroup.employeeApi.exception.ResourceNotFoundException;
import com.fdmgroup.employeeApi.model.Employee;
import com.fdmgroup.employeeApi.service.EmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

	EmployeeService employeeService;

	public EmployeeController(EmployeeService employeeService) {
		super();
		this.employeeService = employeeService;
	}

	@Operation(
			summary = "Return ALL employee objects", 
			method = "GET", 
			responses = {@ApiResponse(
					responseCode = "200", 
					description = "Returns all employees stored in the database.", content = {
							@Content(mediaType = MediaType.APPLICATION_JSON_VALUE) }),
			@ApiResponse(responseCode = "404", description = "There are no employees stored.", content = {
					@Content(mediaType = MediaType.TEXT_PLAIN_VALUE) }) })
	@GetMapping
	public ResponseEntity<List<Employee>> getAllEmployees() {
		return ResponseEntity.ok(employeeService.getAllEmployees());
	}

	@Operation(
			summary = "Finds and returns a single employee by the supplied ID.", 
			description = "Validates employee exists with specified id. Then returns employee found with matching id.", 
			method = "GET", 
			responses = {@ApiResponse(
					responseCode = "200", 
					description = "Returns employee resource succesfully found, employee resource is returned as a "
					+ "JSON object in the body of the response.", content = {
							@Content(mediaType = MediaType.APPLICATION_JSON_VALUE) }),
			@ApiResponse(responseCode = "404", description = "There is no employee resource with the supplied id found.", content = {
					@Content(mediaType = MediaType.TEXT_PLAIN_VALUE) }) })

	@GetMapping("/{id}")
	public ResponseEntity<Employee> getEmployee(@PathVariable int id) {
		return ResponseEntity.status(HttpStatus.OK).body(employeeService.getEmployeeById(id));
	}

	@Operation(
			summary = "Creates a new Employee resource.", 
			description = "Accepts and validates an employee object. Checks the first and last name isn't in the database. Then creates the new employee resource.", 
			method = "POST", 
			responses = {
					@ApiResponse(responseCode = "201", description = "Employee created, location of employee in header.", content = {
							@Content(mediaType = MediaType.APPLICATION_JSON_VALUE) }, headers = {
									@Header(name = HttpHeaders.LOCATION, description = "The location of the new employee resource.", required = true) }),
					@ApiResponse(responseCode = "409", description = "Employee first + last name already in database, conflicts with supplied employee.", content = {
							@Content(mediaType = MediaType.TEXT_PLAIN_VALUE) }),
					@ApiResponse(responseCode = "400", description = "Employee is invalid, returns a CSV string containing validation errors.", content = {
							@Content(mediaType = MediaType.TEXT_PLAIN_VALUE) }) })
	@PostMapping
	public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee employee) {
		employeeService.createEmployee(employee);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(employee.getId())
				.toUri();
		return ResponseEntity.created(location).body(employee);
	}

	@Operation(summary = "Updates and exisiting employee resource.", description = "Validating supplied employee, then checks if supplied ID is valid. Finally employee is updated.", 
			method = "PUT", responses = {
			@ApiResponse(responseCode = "200", description = "Employee updated.", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE) }),
			@ApiResponse(responseCode = "400", description = "Employee is invalid, returns a CSV string containing validation errors.", content = {
					@Content(mediaType = MediaType.TEXT_PLAIN_VALUE) }),
			@ApiResponse(responseCode = "404", description = "No employee with supplied ID found.", content = {
					@Content(mediaType = MediaType.TEXT_PLAIN_VALUE) }) })
	@PutMapping("/{id}")
	public ResponseEntity<Employee> updateEmployee(@Valid @RequestBody Employee employee, @PathVariable int id) {
		return ResponseEntity.ok(employeeService.updateEmployee(employee, id));
	}

	@Operation(summary = "Deletes existing employee resource.", description = "Validating supplied employee, then checks if supplied ID is valid. Finally employee is removed.", 
			method = "DELETE", responses = {
			@ApiResponse(responseCode = "200", description = "Employee removed.", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE) }),
			@ApiResponse(responseCode = "404", description = "No employee with supplied ID found.", content = {
					@Content(mediaType = MediaType.TEXT_PLAIN_VALUE) }) })
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteEmployee(@PathVariable int id) {
		employeeService.deleteById(id);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@ExceptionHandler(value = ResourceNotFoundException.class)
	public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@ExceptionHandler(value = EmployeeExistsException.class)
	public ResponseEntity<String> handleEmployeeExistsException(EmployeeExistsException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
	}

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		List<ObjectError> errors = ex.getAllErrors();
		StringBuilder stringBuilder = new StringBuilder();
		errors.forEach(error -> stringBuilder.append(error.getDefaultMessage() + ","));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(stringBuilder.toString());
	}

}
