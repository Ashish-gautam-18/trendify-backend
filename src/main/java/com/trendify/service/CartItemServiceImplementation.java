package com.trendify.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.trendify.entity.Cart;
import com.trendify.entity.CartItem;
import com.trendify.entity.Product;
import com.trendify.entity.User;
import com.trendify.exception.CartItemException;
import com.trendify.exception.UserException;
import com.trendify.repository.CartItemRepository;

// This service class implements the business logic for managing items inside the customer's cart
@Service
public class CartItemServiceImplementation implements CartItemService {
	
	private CartItemRepository cartItemRepository;
	private UserService userService;
	
	// Constructor injection to safely initialize required dependencies
	public CartItemServiceImplementation(CartItemRepository cartItemRepository, UserService userService) {
		this.cartItemRepository = cartItemRepository;
		this.userService = userService;
	}

	// Sets initial quantity and calculates total price before saving a new item to the cart
	@Override
	public CartItem createCartItem(CartItem cartItem) {
		cartItem.setQuantity(1);
		cartItem.setPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());
		cartItem.setDiscountedPrice(cartItem.getProduct().getDiscountedPrice() * cartItem.getQuantity());
		
		CartItem createdCartItem = cartItemRepository.save(cartItem);
		return createdCartItem;
	}

	// Updates the item quantity and recalculates prices after verifying the correct owner user
	@Override
	public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException, UserException {
		CartItem item = findCartItemById(id);
		User user = userService.findUserById(item.getUserId());
		
		if (user.getId().equals(userId)) {
			item.setQuantity(cartItem.getQuantity());
			item.setPrice(item.getQuantity() * item.getProduct().getPrice());
			item.setDiscountedPrice(item.getQuantity() * item.getProduct().getDiscountedPrice());
			
			return cartItemRepository.save(item);
		} else {
			throw new CartItemException("You can't update another users cart_item");
		}
	}

	// Checks database to see if the specific product and size already exist in the user's cart
	@Override
	public CartItem isCartItemExist(Cart cart, Product product, String size, Long userId) {
		CartItem cartItem = cartItemRepository.isCartItemExist(cart, product, size, userId);
		return cartItem;
	}

	// Removes a specific item from the cart after verifying ownership permissions
	@Override
	public void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException {
		System.out.println("userId- " + userId + " cartItemId " + cartItemId);
		
		CartItem cartItem = findCartItemById(cartItemId);
		User user = userService.findUserById(cartItem.getUserId());
		User reqUser = userService.findUserById(userId);
		
		if (user.getId().equals(reqUser.getId())) {
			cartItemRepository.deleteById(cartItem.getId());
		} else {
			throw new UserException("you can't remove anothor users item");
		}
	}

	// Searches for a cart item by its ID and returns it, or throws an exception if not found
	@Override
	public CartItem findCartItemById(Long cartItemId) throws CartItemException {
		Optional<CartItem> opt = cartItemRepository.findById(cartItemId);
		
		if (opt.isPresent()) {
			return opt.get();
		}
		throw new CartItemException("cartItem not found with id : " + cartItemId);
	}

}
