package com.trendify.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.trendify.entity.OrderItem;

// This interface provides standard database operations (Save, Find, Delete) for individual items inside an order
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
