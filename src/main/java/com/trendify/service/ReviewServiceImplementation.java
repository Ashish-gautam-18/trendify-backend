package com.trendify.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.trendify.entity.Product;
import com.trendify.entity.Review;
import com.trendify.entity.User;
import com.trendify.exception.ProductException;
import com.trendify.repository.ProductRepository;
import com.trendify.repository.ReviewRepository;
import com.trendify.request.ReviewRequest;

// This service class implements the business logic for creating and fetching product reviews
@Service
public class ReviewServiceImplementation implements ReviewService {
	
	private ReviewRepository reviewRepository;
	private ProductService productService;
	private ProductRepository productRepository;
	
	// Constructor injection to safely load repositories and product service operations
	public ReviewServiceImplementation(ReviewRepository reviewRepository, ProductService productService, ProductRepository productRepository) {
		this.reviewRepository = reviewRepository;
		this.productService = productService;
		this.productRepository = productRepository;
	}

	// Finds a product, updates its record, and saves a newly submitted customer review text
	@Override
	public Review createReview(ReviewRequest req, User user) throws ProductException {
		Product product = productService.findProductById(req.getProductId());
		Review review = new Review();
		review.setUser(user);
		review.setProduct(product);
		review.setReview(req.getReview());
		review.setCreatedAt(LocalDateTime.now());
		
		productRepository.save(product);
		return reviewRepository.save(review);
	}

	// Retrieves all written product reviews for a specific product ID from the database
	@Override
	public List<Review> getAllReview(Long productId) {
		return reviewRepository.getAllProductsReview(productId);
	}

}
