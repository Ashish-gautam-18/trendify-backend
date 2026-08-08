package com.trendify.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.trendify.entity.Category;
import com.trendify.entity.Product;
import com.trendify.exception.ProductException;
import com.trendify.repository.CategoryRepository;
import com.trendify.repository.ProductRepository;
import com.trendify.request.CreateProductRequest;

// This service class implements the business logic operations for managing store products
@Service
public class ProductServiceImplementation implements ProductService {
	
	private ProductRepository productRepository;
	private CategoryRepository categoryRepository;
	
	// Constructor injection to load database repositories safely
	public ProductServiceImplementation(ProductRepository productRepository, CategoryRepository categoryRepository) {
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
	}

	// Creates a product and dynamically maps it to a 3-level deep category hierarchy
	@Override
	public Product createProduct(CreateProductRequest req) {
		Category topLevel = categoryRepository.findByName(req.getTopLavelCategory());
		
		if (topLevel == null) {
			Category topLavelCategory = new Category();
			topLavelCategory.setName(req.getTopLavelCategory());
			topLavelCategory.setLevel(1);
			topLevel = categoryRepository.save(topLavelCategory);
		}
		
		Category secondLevel = categoryRepository.findByNameAndParant(req.getSecondLavelCategory(), topLevel.getName());
		if (secondLevel == null) {
			Category secondLavelCategory = new Category();
			secondLavelCategory.setName(req.getSecondLavelCategory());
			secondLavelCategory.setParentCategory(topLevel);
			secondLavelCategory.setLevel(2);
			secondLevel = categoryRepository.save(secondLavelCategory);
		}

		Category thirdLevel = categoryRepository.findByNameAndParant(req.getThirdLavelCategory(), secondLevel.getName());
		if (thirdLevel == null) {
			Category thirdLavelCategory = new Category();
			thirdLavelCategory.setName(req.getThirdLavelCategory());
			thirdLavelCategory.setParentCategory(secondLevel);
			thirdLavelCategory.setLevel(3);
			thirdLevel = categoryRepository.save(thirdLavelCategory);
		}
		
		Product product = new Product();
		product.setTitle(req.getTitle());
		product.setColor(req.getColor());
		product.setDescription(req.getDescription());
		product.setDiscountedPrice(req.getDiscountedPrice());
		product.setDiscountPersent(req.getDiscountPersent());
		product.setImageUrl(req.getImageUrl());
		product.setBrand(req.getBrand());
		product.setPrice(req.getPrice());
		product.setSizes(req.getSize());
		product.setQuantity(req.getQuantity());
		product.setCategory(thirdLevel);
		product.setCreatedAt(LocalDateTime.now());
		
		Product savedProduct = productRepository.save(product);
		System.out.println("products - " + product);
		
		return savedProduct;
	}

	// Deletes a specific product from the database after clearing its product sizes list
	@Override
	public String deleteProduct(Long productId) throws ProductException {
		Product product = findProductById(productId);
		System.out.println("delete product " + product.getId() + " - " + productId);
		product.getSizes().clear();
		productRepository.delete(product);
		
		return "Product deleted Successfully";
	}

	// Updates an existing product's quantity and description if they are provided in the request
	@Override
	public Product updateProduct(Long productId, Product req) throws ProductException {
		Product product = findProductById(productId);
		
		if (req.getQuantity() != 0) {
			product.setQuantity(req.getQuantity());
		}
		if (req.getDescription() != null) {
			product.setDescription(req.getDescription());
		}
		
		return productRepository.save(product);
	}

	// Fetches and returns all products available in the system
	@Override
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	// Searches for a product by its ID and returns it, or throws a ProductException if not found
	@Override
	public Product findProductById(Long id) throws ProductException {
		Optional<Product> opt = productRepository.findById(id);
		
		if (opt.isPresent()) {
			return opt.get();
		}
		throw new ProductException("product not found with id " + id);
	}

	// Finds all products belonging to a specific text category name
	@Override
	public List<Product> findProductByCategory(String category) {
		System.out.println("category --- " + category);
		List<Product> products = productRepository.findByCategory(category);
		return products;
	}

	// Searches for products in the database using a custom query keyword string
	@Override
	public List<Product> searchProduct(String query) {
		List<Product> products = productRepository.searchProduct(query);
		return products;
	}

	// Filters products dynamically based on colors, stock status, and returns a paginated view
	@Override
	public Page<Product> getAllProduct(String category, List<String> colors, 
			List<String> sizes, Integer minPrice, Integer maxPrice, 
			Integer minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize) {

		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		List<Product> products = productRepository.filterProducts(category, minPrice, maxPrice, minDiscount, sort);
		
		// Filter by color list if it contains elements
		if (!colors.isEmpty()) {
			products = products.stream()
			        .filter(p -> colors.stream().anyMatch(c -> c.equalsIgnoreCase(p.getColor())))
			        .collect(Collectors.toList());
		} 

		// Filter by stock level string value
		if (stock != null) {
			if (stock.equals("in_stock")) {
				products = products.stream().filter(p -> p.getQuantity() > 0).collect(Collectors.toList());
			} else if (stock.equals("out_of_stock")) {
				products = products.stream().filter(p -> p.getQuantity() < 1).collect(Collectors.toList());				
			}
		}
		
		int startIndex = (int) pageable.getOffset();
		int endIndex = Math.min(startIndex + pageable.getPageSize(), products.size());

		List<Product> pageContent = products.subList(startIndex, endIndex);
		Page<Product> filteredProducts = new PageImpl<>(pageContent, pageable, products.size());
	    return filteredProducts;
	}

	// Fetches the top 10 most recently added products sorted by creation time
	@Override
	public List<Product> recentlyAddedProduct() {
		return productRepository.findTop10ByOrderByCreatedAtDesc();
	}

}
