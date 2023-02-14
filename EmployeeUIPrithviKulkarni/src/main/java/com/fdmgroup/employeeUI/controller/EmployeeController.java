package com.fdmgroup.employeeUI.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fdmgroup.employeeUI.exception.GenericEmployeeException;
import com.fdmgroup.employeeUI.exception.InvalidEmployeeException;
import com.fdmgroup.employeeUI.model.Employee;
import com.fdmgroup.employeeUI.service.FeignEmployeeService;
import com.fdmgroup.employeeUI.service.WebClientEmployeeService;

@Controller
public class EmployeeController {
	private final FeignEmployeeService employeeService;
	
	public EmployeeController(FeignEmployeeService employeeService) {
		super();
		this.employeeService = employeeService;
	}

	@RequestMapping("allEmployees")
	public String allEmployees(Model model) {
		model.addAttribute("listOfEmployees", employeeService.getAllEmployees());
		return "all-employees";
	}
	
	@GetMapping("editEmployee")
	public String editEmployee(@RequestParam int id, Model model) {
		Employee employee = employeeService.getEmployeeById(id);
		// maybe add a session attribute here to track the ID of the employee being edited rather than relying on the model attr
		model.addAttribute("employee", employee);
		return "edit-employee";
	}
	
	@PostMapping("editEmployeeSubmit")
	public String editEmployeeSubmit(@ModelAttribute Employee employee) {
		employeeService.updateEmployee(employee, employee.getId());
		return "forward:/allEmployees";
	}
	
	@GetMapping("addEmployee")
	public String addEmployee(Model model) {
		model.addAttribute("employee", new Employee());
		return "add-employee";
	}
	
	@PostMapping("addEmployeeSubmit")
	public String addEmployeeSubmit(@ModelAttribute Employee employee) {
		employeeService.addEmployee(employee);
		return "forward:/allEmployees";
	}
	
	@GetMapping("deleteEmployee")
	public String deleteEmployee(@RequestParam int id) {
		employeeService.deleteEmployee(id);
		return "forward:/allEmployees";
	}
	
	@ExceptionHandler(GenericEmployeeException.class)
	public String handleGenericEmployeeException(GenericEmployeeException ex, Model model) {
		model.addAttribute("errorCode", ex.STATUS);
		model.addAttribute("errorMessage", ex.getMessage());
		return "error-page";
	}
	
	@ExceptionHandler(InvalidEmployeeException.class)
	public String handleInvalidEmployeeException(InvalidEmployeeException ex, Model model) {
		model.addAttribute("validationErrors", ex.getErrorMessages());
		return addEmployee(model);
	}
}
