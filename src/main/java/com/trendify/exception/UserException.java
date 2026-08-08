package com.trendify.exception;

// This custom exception handles all errors related to Users and Authentication
public class UserException extends Exception {

	// Constructor to create the exception with a specific error message
	public UserException(String message) {
		super(message);
	}
}
