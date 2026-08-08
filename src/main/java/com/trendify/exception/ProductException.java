package com.trendify.exception;

// This custom exception handles all errors related to Products
public class ProductException extends Exception {
	
	// Constructor to create the exception with a specific error message
	public ProductException(String message) {
		super(message);
	}

}
