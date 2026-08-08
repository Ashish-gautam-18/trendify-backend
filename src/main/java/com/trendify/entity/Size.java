package com.trendify.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

// This class represents the size option of a product (e.g., S, M, L)
// @Embeddable means its fields will be stored inside the parent Product table's collection
@Embeddable
@Data
public class Size {

	// The name of the size variant (e.g., "M", "L", "XL")
	private String name;
	
	// The total available stock quantity for this specific size
	private int quantity;

}
