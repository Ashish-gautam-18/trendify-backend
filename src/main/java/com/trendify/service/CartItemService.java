package com.trendify.service;

import com.trendify.entity.Cart;
import com.trendify.entity.CartItem;
import com.trendify.entity.Product;
import com.trendify.exception.CartItemException;
import com.trendify.exception.UserException;

// This interface defines the business logic operations for managing items inside a shopping cart
public interface CartItemService {
	
	// Saves or creates a new item to be placed inside the customer's cart
	CartItem createCartItem(CartItem cartItem);
	
	// Updates the quantity or details of an existing cart item after verifying the owner user ID
	CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException, UserException;
	
	// Checks if a specific product with a particular size already exists in the user's cart
	CartItem isCartItemExist(Cart cart, Product product, String size, Long userId);
	
	// Removes an item completely from the shopping cart based on its unique ID
	void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException;
	
	// Fetches and finds a specific cart item details using its unique ID
	CartItem findCartItemById(Long cartItemId) throws CartItemException;
	
}
