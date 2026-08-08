package com.trendify.exception;

import java.time.LocalDateTime;

// This class represents the structure of error messages sent back to the frontend
public class ErrorDetails {
	
	// The short error title or message
	private String error;

	// Detailed description of what caused the error
	private String details;

	// The specific date and time when the error happened
	private LocalDateTime timestamp;

	// Default no-argument constructor
	public ErrorDetails() {
	}

	// Parameterized constructor to quickly initialize all error fields
	public ErrorDetails(String error, String details, LocalDateTime timestamp) {
		super();
		this.error = error;
		this.details = details;
		this.timestamp = timestamp;
	}

	// Getter method for error title
	public String getError() {
		return error;
	}

	// Setter method for error title
	public void setError(String error) {
		this.error = error;
	}

	// Getter method for error details
	public String getDetails() {
		return details;
	}

	// Setter method for error details
	public void setDetails(String details) {
		this.details = details;
	}

	// Getter method for error timestamp
	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	// Setter method for error timestamp
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	
}



