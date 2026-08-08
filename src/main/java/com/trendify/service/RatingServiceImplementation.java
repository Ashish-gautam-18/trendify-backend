package com.trendify.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.trendify.entity.Product;
import com.trendify.entity.Rating;
import com.trendify.entity.User;
import com.trendify.exception.ProductException;
import com.trendify.repository.RatingRepository;
import com.trendify.request.RatingRequest;

// This service class implements the business logic for creating and retrieving product ratings
@Service
public class RatingServiceImplementation implements RatingServices {
	
	private RatingRepository ratingRepository;
	private ProductService productService;
	
	// Constructor injection to wire required repository and product service dependencies
	public RatingServiceImplementation(RatingRepository ratingRepository, ProductService productService) {
		this.ratingRepository = ratingRepository;
		this.productService = productService;
	}

	// Finds the product and saves a new customer rating with the current system date and time
	@Override
	public Rating createRating(RatingRequest req, User user) throws ProductException {
		
		Product product = productService.findProductById(req.getProductId());
		
		Rating rating = new Rating();
		rating.setProduct(product);
		rating.setUser(user);
		rating.setRating(req.getRating());
		rating.setCreatedAt(LocalDateTime.now());
		
		return ratingRepository.save(rating);
	}

	// Fetches a complete list of ratings linked to a specific product from the database
	@Override
	public List<Rating> getProductsRating(Long productId) {
		return ratingRepository.getAllProductsRating(productId);
	}

}
