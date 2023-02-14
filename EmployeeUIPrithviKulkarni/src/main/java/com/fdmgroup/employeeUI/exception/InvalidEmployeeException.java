package com.fdmgroup.employeeUI.exception;

import java.util.Arrays;
import java.util.List;

public class InvalidEmployeeException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final List<String> errorMessages;
	
	public InvalidEmployeeException(String csvErrors) {
		super("Invalid employee.");
		this.errorMessages = Arrays.asList(csvErrors.split(","));
	}

	public List<String> getErrorMessages() {
		return errorMessages;
	}

}
