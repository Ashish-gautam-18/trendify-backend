
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

import com.trendify.entity.Review;
import com.trendify.entity.User;
import com.trendify.exception.ProductException;
import com.trendify.exception.UserException;
import com.trendify.request.ReviewRequest;
import com.trendify.service.ReviewService;
import com.trendify.service.UserService;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
	
	private ReviewService reviewService;
	private UserService userService;
	
	// Constructor to inject review and user services
	public ReviewController(ReviewService reviewService,UserService userService) {
		this.reviewService=reviewService;
		this.userService=userService;
	}
	
	// API endpoint for a logged-in user to write and submit a product review
	@PostMapping("/create")
	public ResponseEntity<Review> createReviewHandler(@RequestBody ReviewRequest req,@RequestHeader("Authorization") String jwt) throws UserException, ProductException{
		
		// Identify user from the incoming JWT token
		User user=userService.findUserProfileByJwt(jwt);
		
		System.out.println("product id "+req.getProductId()+" - "+req.getReview());
		
		// Create and store the text review in database
		Review review=reviewService.createReview(req, user);
		System.out.println("product review "+req.getReview());
		
		return new ResponseEntity<Review>(review,HttpStatus.ACCEPTED);
	}
	
	// API endpoint to fetch all written reviews for a specific product
	@GetMapping("/product/{productId}")
	public ResponseEntity<List<Review>> getProductsReviewHandler(@PathVariable Long productId){
		
		// Retrieve reviews list from service layer using product ID
		List<Review>reviews=reviewService.getAllReview(productId);
		return new ResponseEntity<List<Review>>(reviews,HttpStatus.OK);
	}

}

