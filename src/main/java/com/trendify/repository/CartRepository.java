package com.trendify.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.trendify.entity.Cart;

// This interface manages database operations for the customer Shopping Cart
public interface CartRepository extends JpaRepository<Cart, Long> {

	// Custom database query to find a shopping cart belonging to a specific user ID
	@Query("SELECT c From Cart c where c.user.id=:userId")
	public Cart findByUserId(@Param("userId") Long userId);
}
