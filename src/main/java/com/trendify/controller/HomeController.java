package com.trendify.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trendify.response.ApiResponse;

@RestController
public class HomeController {

	// API endpoint for the main landing page of the application
	@GetMapping("/")
	public ResponseEntity<ApiResponse> homeController(){
		
		// Create a standard welcome response object
		ApiResponse res=new ApiResponse("Welcome To E-Commerce System", true);
		
		return new ResponseEntity<>(res,HttpStatus.OK);
	}
}
