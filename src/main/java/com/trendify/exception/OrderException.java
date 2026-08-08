package com.trendify.exception;

// This custom exception handles all errors related to customer Orders
public class OrderException extends Exception {
	
	// Constructor to create the exception with a specific error message
	public OrderException(String message) {
		super(message);
	}

}
