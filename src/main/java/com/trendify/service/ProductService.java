package com.trendify.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.trendify.entity.Product;
import com.trendify.exception.ProductException;
import com.trendify.request.CreateProductRequest;

// This interface defines the core business logic operations for managing products in the store
public interface ProductService {
	
	// Creates and saves a new product record in the system (Restricted to Admin only)
	Product createProduct(CreateProductRequest req) throws ProductException;
	
	// Deletes a specific product from the database using its unique ID (Restricted to Admin only)
	String deleteProduct(Long productId) throws ProductException;
	
	// Updates the details of an existing product based on its ID (Restricted to Admin only)
	Product updateProduct(Long productId, Product product) throws ProductException;
	
	// Fetches and returns a complete list of all products available in the database
	List<Product> getAllProducts();
	
	// Searches and retrieves the full details of a specific product using its unique ID
	Product findProductById(Long id) throws ProductException;
	
	// Retrieves a list of all products that belongs to a specific main category name
	List<Product> findProductByCategory(String category);
	
	// Performs a text-based search to find products matching the user's search query keywords
	List<Product> searchProduct(String query);
	
	// Filters and paginates products based on category, colors, sizes, price ranges, discount, sorting, and stock status
	Page<Product> getAllProduct(String category, List<String> colors, List<String> sizes, Integer minPrice, Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize);
	
	// Retrieves a list of the most recently added or newest products to display on the storefront
	List<Product> recentlyAddedProduct();

}
