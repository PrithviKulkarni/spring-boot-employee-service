package com.fdmgroup.employeeApi.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

import com.fdmgroup.employeeApi.exception.EmployeeExistsException;
import com.fdmgroup.employeeApi.exception.ResourceNotFoundException;
import com.fdmgroup.employeeApi.model.Employee;
import com.fdmgroup.employeeApi.repository.EmployeeRepository;

@Service
public class EmployeeService {

	private EmployeeRepository employeeRepo;

	public EmployeeService(EmployeeRepository employeeRepo) {
		super();
		this.employeeRepo = employeeRepo;
	}

	public List<Employee> getAllEmployees() {
		return employeeRepo.findAll();
	}

	public Employee getEmployeeById(int id) {
		Optional<Employee> employeeOpt = employeeRepo.findById(id);
		if (employeeOpt.isEmpty()) {
			throw new ResourceNotFoundException(getMessage(id));
		} else {
			return employeeOpt.get();
		}

	}

	public Employee createEmployee(Employee employee) {
		Optional<Employee> employeeOpt = employeeRepo.findByFirstAndLastName(employee.getFirstName(),
				employee.getLastName());
		if (employeeOpt.isPresent()) {
			throw new EmployeeExistsException("Employee with name " + employee.getFirstName() + " "
					+ employee.getLastName() + " already exists.");
		} else {
			return employeeRepo.save(employee);
		}
	}

	public Employee updateEmployee(Employee employee, int id) {
		Optional<Employee> employeeOpt = employeeRepo.findById(id);
		if (employeeOpt.isEmpty()) {
			throw new ResourceNotFoundException(getMessage(id));
		} else {
			employee.setId(id);
			return employeeRepo.save(employee);
		}
	}

	public void deleteById(int id) {
		Optional<Employee> employeeOpt = employeeRepo.findById(id);
		if (employeeOpt.isEmpty()) {
			throw new ResourceNotFoundException(getMessage(id));
		} else {
			employeeRepo.deleteById(id);
		}
	}

	private String getMessage(int id) {
		return "Employee of id: " + id + " not found.";
	}
}
