package com.fdmgroup.employeeApi.exception;

public class EmployeeExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EmployeeExistsException(String message) {
		super(message);
	}
}
