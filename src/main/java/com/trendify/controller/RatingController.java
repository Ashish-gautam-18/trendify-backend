package com.trendify.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trendify.entity.Rating;
// REMOVED UNUSED IMPORT: com.trendify.entity.Review;
import com.trendify.entity.User;
import com.trendify.exception.ProductException;
import com.trendify.exception.UserException;
import com.trendify.request.RatingRequest;
import com.trendify.service.RatingServices;
import com.trendify.service.UserService;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {
	
	private UserService userService;
	private RatingServices ratingServices;
	
	// Constructor to inject user and rating services
	public RatingController(UserService userService,RatingServices ratingServices) {
		this.ratingServices=ratingServices;
		this.userService=userService;
	}

	// API endpoint for logged-in user to submit a rating for a product
	@PostMapping("/create")
	public ResponseEntity<Rating> createRatingHandler(@RequestBody RatingRequest req,@RequestHeader("Authorization") String jwt) throws UserException, ProductException{
		
		// Find user identity using the incoming JWT token
		User user=userService.findUserProfileByJwt(jwt);
		
		// Create and save the rating details in the database
		Rating rating=ratingServices.createRating(req, user);
		return new ResponseEntity<>(rating,HttpStatus.ACCEPTED);
	}
	
	// API endpoint to fetch all ratings submitted for a specific product
	@GetMapping("/product/{productId}")
	public ResponseEntity<List<Rating>> getProductsReviewHandler(@PathVariable Long productId){
	
		// Retrieve lists of ratings using product ID
		List<Rating> ratings=ratingServices.getProductsRating(productId);
		return new ResponseEntity<>(ratings,HttpStatus.OK);
	}
}
