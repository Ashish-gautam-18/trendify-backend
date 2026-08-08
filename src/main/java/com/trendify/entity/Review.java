package com.trendify.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// This class represents the customer Reviews table in the database
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {
	
	// Unique ID for each written review record
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	// The actual text message or review comment written by the customer
	private String review;
	
	// Many reviews can belong to a single Product
	// @JsonIgnore stops loops when converting product and review data to JSON
	@ManyToOne
	@JoinColumn(name="product_id")
	@JsonIgnore
	private Product product;

	// Many reviews can be written by a single User
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	// Timestamp showing exactly when the customer submitted this review
	private LocalDateTime createdAt;

}
