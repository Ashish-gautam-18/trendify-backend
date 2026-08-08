package com.trendify.service;

import com.trendify.entity.Cart;
import com.trendify.entity.CartItem;
import com.trendify.entity.User;
import com.trendify.exception.ProductException;
import com.trendify.request.AddItemRequest;

// This interface defines the core business operations for managing a user's shopping cart
public interface CartService {
	
	// Creates a new shopping cart and assigns it to a registered user
	Cart createCart(User user);
	
	// Adds a new product item into the user's cart using the incoming request details
	CartItem addCartItem(Long userId, AddItemRequest req) throws ProductException;
	
	// Finds and retrieves the active shopping cart linked to a specific user ID
	Cart findUserCart(Long userId);

}
