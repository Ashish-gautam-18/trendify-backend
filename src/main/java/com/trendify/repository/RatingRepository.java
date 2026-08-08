package com.trendify.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.trendify.entity.Rating;

// This interface manages database operations for product Ratings
public interface RatingRepository extends JpaRepository<Rating, Long> {
	
	// Custom query to fetch all ratings given to a specific product ID
	@Query("Select r From Rating r where r.product.id=:productId")
	public List<Rating> getAllProductsRating(@Param("productId") Long productId);

}
