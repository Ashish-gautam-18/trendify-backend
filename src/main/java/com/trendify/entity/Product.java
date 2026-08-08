package com.trendify.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// This class represents the Product table in the database
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

	// Unique ID for each product record
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	// Title or name of the product
	@Column(name = "title")
	private String title;

	// Detailed description of the product
	@Column(name = "description")
	private String description;

	// Original standard price of the product
	@Column(name = "price")
	private int price;

	// Final price after deducting the discount
	@Column(name = "discounted_price")
	private int discountedPrice;
    
	// The discount percentage applied to the product
	@Column(name="discount_persent")
	private int discountPersent;

	// Total available stock or quantity in the inventory
	@Column(name = "quantity")
	private int quantity;

	// Brand name of the product
	@Column(name = "brand")
	private String brand;

	// Primary color of the product
	@Column(name = "color")
	private String color;

	// A collection of available sizes (e.g., S, M, L) stored as a separate collection table
	@ElementCollection
	@Column(name = "sizes")
	private Set<Size> sizes = new HashSet<>();

	// Direct URL link to the product's image
	@Column(name = "image_url")
	private String imageUrl;

	// One product can have multiple customer ratings
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Rating> ratings = new ArrayList<>();
    
	// One product can have multiple written customer reviews
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Review> reviews = new ArrayList<>();

	// The total count or number of ratings this product has received
	@Column(name = "num_ratings")
	private int numRatings;
    
	// Many products can belong to a single Category
	@ManyToOne()
	@JoinColumn(name="category_id")
	private Category category;
    
	// Timestamp showing when the product was added to the website
	private LocalDateTime createdAt;

}


