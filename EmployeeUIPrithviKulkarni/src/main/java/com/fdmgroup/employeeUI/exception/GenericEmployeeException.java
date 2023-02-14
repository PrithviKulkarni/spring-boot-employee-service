package com.fdmgroup.employeeUI.exception;

public class GenericEmployeeException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	public final String STATUS;

	public GenericEmployeeException(String message, String status) {
		super(message);
		STATUS = status;
	}
}
