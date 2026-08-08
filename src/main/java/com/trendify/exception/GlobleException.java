package com.trendify.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

// This class intercepts and handles all backend exceptions globally across the application
@ControllerAdvice
public class GlobleException {

	// Handles errors when a requested User is not found or has invalid data
	@ExceptionHandler(UserException.class)
	public ResponseEntity<ErrorDetails> UserExceptionHandler(UserException ue, WebRequest req){

		ErrorDetails err = new ErrorDetails(ue.getMessage(), req.getDescription(false), LocalDateTime.now());

		return new ResponseEntity<ErrorDetails>(err, HttpStatus.BAD_REQUEST);

	}

	// Handles errors related to Products (e.g., product not found in inventory)
	@ExceptionHandler(ProductException.class)
	public ResponseEntity<ErrorDetails> ProductExceptionHandler(ProductException ue, WebRequest req){

		ErrorDetails err = new ErrorDetails(ue.getMessage(), req.getDescription(false), LocalDateTime.now());

		return new ResponseEntity<ErrorDetails>(err, HttpStatus.BAD_REQUEST);

	}

	// Handles errors related to Cart Items (e.g., modifying an item that does not exist)
	@ExceptionHandler(CartItemException.class)
	public ResponseEntity<ErrorDetails> CartItemExceptionHandler(CartItemException ue, WebRequest req){

		ErrorDetails err = new ErrorDetails(ue.getMessage(), req.getDescription(false), LocalDateTime.now());

		return new ResponseEntity<ErrorDetails>(err, HttpStatus.BAD_REQUEST);

	}

	// Handles errors related to customer Orders (e.g., checkout failures)
	@ExceptionHandler(OrderException.class)
	public ResponseEntity<ErrorDetails> OrderExceptionHandler(OrderException ue, WebRequest req){

		ErrorDetails err = new ErrorDetails(ue.getMessage(), req.getDescription(false), LocalDateTime.now());

		return new ResponseEntity<ErrorDetails>(err, HttpStatus.BAD_REQUEST);

	}

	// Handles validation errors when inputs from the frontend fail Jakarta validation checks
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorDetails> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException me){
		ErrorDetails err = new ErrorDetails(me.getBindingResult().getFieldError().getDefaultMessage(), "validation error", LocalDateTime.now());
		return new ResponseEntity<ErrorDetails>(err, HttpStatus.BAD_REQUEST);
	}

	// Handles 404 errors when a requested URL or API endpoint does not exist
	@ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, java.net.http.HttpHeaders headers, HttpStatus status, WebRequest request) {
        java.util.Map<String, Object> body = new java.util.LinkedHashMap<>();
        body.put("message", "Endpoint not found");
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

	// Global fallback handler to catch any other unhandled general java exceptions safely
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDetails> otherExceptionHandler(Exception e, WebRequest req){
		ErrorDetails error = new ErrorDetails(e.getMessage(), req.getDescription(false), LocalDateTime.now());

		return new ResponseEntity<ErrorDetails>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}