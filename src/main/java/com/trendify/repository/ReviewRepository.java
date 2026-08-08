package com.trendify.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.trendify.entity.Review;

// This interface manages database operations for customer Reviews
public interface ReviewRepository extends JpaRepository<Review, Long> {

	// Custom query to fetch all reviews given to a specific product ID (Fixed 'Rating' to 'Review')
	@Query("Select r from Review r where r.product.id=:productId")
	public List<Review> getAllProductsReview(@Param("productId") Long productId);
}
