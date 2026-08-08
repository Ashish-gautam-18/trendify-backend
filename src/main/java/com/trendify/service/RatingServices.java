package com.trendify.service;

import java.util.List;

import com.trendify.entity.Rating;
import com.trendify.entity.User;
import com.trendify.exception.ProductException;
import com.trendify.request.RatingRequest;

// This interface defines the core business operations for managing product ratings given by users
public interface RatingServices {
	
	// Creates and saves a new rating for a product based on the user's request details
	Rating createRating(RatingRequest req, User user) throws ProductException;
	
	// Retrieves a complete list of all ratings given to a specific product by its ID
	List<Rating> getProductsRating(Long productId);

}
