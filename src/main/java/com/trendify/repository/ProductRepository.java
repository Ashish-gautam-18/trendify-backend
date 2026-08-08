package com.trendify.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.trendify.entity.Product;

// This interface manages database operations for Products
public interface ProductRepository extends JpaRepository<Product, Long> {

	// Finds all products matching a specific category name (converted to lowercase)
	@Query("SELECT p From Product p Where LOWER(p.category.name)=:category")
	public List<Product> findByCategory(@Param("category") String category);
	
	// Searches products by checking if the query matches the title, description, brand, or category
	@Query("SELECT p From Product p where LOWER(p.title) Like %:query% OR LOWER(p.description) Like %:query% OR LOWER(p.brand) LIKE %:query% OR LOWER(p.category.name) LIKE %:query%")
	public List<Product> searchProduct(@Param("query") String query);

	// Advanced filter query to search products by category, price range, minimum discount, and sorting order
	@Query("SELECT p FROM Product p " +
	        "WHERE (p.category.name = :category OR :category = '') " +
	        "AND ((:minPrice IS NULL AND :maxPrice IS NULL) OR (p.discountedPrice BETWEEN :minPrice AND :maxPrice)) " +
		    "AND (:minDiscount IS NULL OR p.discountPersent >= :minDiscount) " +
		    "ORDER BY " +
		    "CASE WHEN :sort = 'price_low' THEN p.discountedPrice END ASC, " +
		    "CASE WHEN :sort = 'price_high' THEN p.discountedPrice END DESC, "+
		    "p.createdAt DESC")
	List<Product> filterProducts(
	        @Param("category") String category,
			@Param("minPrice") Integer minPrice,
			@Param("maxPrice") Integer maxPrice,
			@Param("minDiscount") Integer minDiscount,
			@Param("sort") String sort
			);
	
	// Fetches the top 10 newly added products sorted by their creation date
	public List<Product> findTop10ByOrderByCreatedAtDesc();
}
