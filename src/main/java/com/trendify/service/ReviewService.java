package com.trendify.service;

import java.util.List;

import com.trendify.entity.Review;
import com.trendify.entity.User;
import com.trendify.exception.ProductException;
import com.trendify.request.ReviewRequest;

// This interface defines the core business operations for managing customer product reviews
public interface ReviewService {

	// Creates and saves a new written review for a product based on the incoming request details
	Review createReview(ReviewRequest req, User user) throws ProductException;
	
	// Retrieves a complete list of all written reviews written for a specific product ID
	List<Review> getAllReview(Long productId);
	
}
