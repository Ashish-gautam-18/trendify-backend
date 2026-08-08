package com.trendify.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.trendify.entity.Address;
import com.trendify.entity.Cart;
import com.trendify.entity.CartItem;
import com.trendify.entity.Order;
import com.trendify.entity.OrderItem;
import com.trendify.entity.User;
import com.trendify.exception.OrderException;
import com.trendify.repository.AddressRepository;
import com.trendify.repository.OrderItemRepository;
import com.trendify.repository.OrderRepository;
import com.trendify.repository.UserRepository;
import com.trendify.user.domain.OrderStatus;
import com.trendify.user.domain.PaymentStatus;

@Service
public class OrderServiceImplementation implements OrderService {

	private OrderRepository orderRepository;
	private CartService cartService;
	private AddressRepository addressRepository;
	private UserRepository userRepository;
	private OrderItemService orderItemService;
	private OrderItemRepository orderItemRepository;

	public OrderServiceImplementation(OrderRepository orderRepository, CartService cartService,
			AddressRepository addressRepository, UserRepository userRepository,
			OrderItemService orderItemService, OrderItemRepository orderItemRepository) {
		this.orderRepository = orderRepository;
		this.cartService = cartService;
		this.addressRepository = addressRepository;
		this.userRepository = userRepository;
		this.orderItemService = orderItemService;
		this.orderItemRepository = orderItemRepository;
	}

	@Override
	public Order createOrder(User user, Address shippAddress) {

		// FIX: Ab hum blindly frontend se aayi Address ko save nahi kar rahe.
		// Pehle check karte hain ki iska ID valid hai aur DB me sach me exist karta hai,
		// aur wo address isi user ka hai. Agar ID stale/invalid hai (jaise purana cached
		// data DB ke sath match nahi kar raha), to naya address create kar dete hain
		// taaki FK constraint kabhi fail na ho.
		Address address = resolveAddress(shippAddress, user);

		Cart cart = cartService.findUserCart(user.getId());
		List<OrderItem> orderItems = new ArrayList<>();

		for (CartItem item : cart.getCartItems()) {
			OrderItem orderItem = new OrderItem();

			orderItem.setPrice(item.getPrice());
			orderItem.setProduct(item.getProduct());
			orderItem.setQuantity(item.getQuantity());
			orderItem.setSize(item.getSize());
			orderItem.setUserId(item.getUserId());
			orderItem.setDiscountedPrice(item.getDiscountedPrice());

			OrderItem createdOrderItem = orderItemRepository.save(orderItem);

			orderItems.add(createdOrderItem);
		}

		Order createdOrder = new Order();
		createdOrder.setUser(user);
		createdOrder.setOrderItems(orderItems);
		createdOrder.setTotalPrice(cart.getTotalPrice());
		createdOrder.setTotalDiscountedPrice(cart.getTotalDiscountedPrice());
		createdOrder.setDiscount(cart.getDiscounte());
		createdOrder.setTotalItem(cart.getTotalItem());

		createdOrder.setShippingAddress(address);
		createdOrder.setOrderDate(LocalDate.now());
		createdOrder.setOrderStatus(OrderStatus.PENDING);
		createdOrder.getPaymentDetails().setStatus(PaymentStatus.PENDING);
		createdOrder.setCreatedAt(LocalDateTime.now());

		Order savedOrder = orderRepository.save(createdOrder);

		for (OrderItem item : orderItems) {
			item.setOrder(savedOrder);
			orderItemRepository.save(item);
		}

		return savedOrder;

	}

	// FIX: Naya helper method — address ko safely resolve karta hai.
	// Case 1: Agar frontend se address ke sath ek ID aayi hai, to us ID ko DB me
	//         dhoondte hain. Agar mil jaye aur wo isi user ki ho, to wahi (managed/
	//         valid) address use karte hain — koi naya insert nahi hota.
	// Case 2: Agar ID nahi aayi (naya address form) YA ID DB me exist nahi karti
	//         (stale/invalid cached data), to isse ek NAYE address ke roop me treat
	//         karke fresh save karte hain — taaki generated ID hamesha valid rahe.
	private Address resolveAddress(Address shippAddress, User user) {

		if (shippAddress.getId() != null) {
			Optional<Address> existingOpt = addressRepository.findById(shippAddress.getId());

			if (existingOpt.isPresent()) {
				Address existing = existingOpt.get();

				boolean belongsToUser = existing.getUser() != null
						&& existing.getUser().getId().equals(user.getId());

				if (belongsToUser) {
					return existing; // valid, existing address — reuse it
				}
			}
			// ID diya gaya tha lekin DB me nahi mila, ya kisi aur user ka hai
			// -> ise naye address ki tarah treat karo (ID reset karke)
			shippAddress.setId(null);
		}

		// Naya address save karo
		shippAddress.setUser(user);
		Address newAddress = addressRepository.save(shippAddress);
		user.getAddresses().add(newAddress);
		userRepository.save(user);
		return newAddress;
	}

	@Override
	public Order placedOrder(Long orderId) throws OrderException {
		Order order = findOrderById(orderId);
		order.setOrderStatus(OrderStatus.PLACED);
		order.getPaymentDetails().setStatus(PaymentStatus.COMPLETED);
		return order;
	}

	@Override
	public Order confirmedOrder(Long orderId) throws OrderException {
		Order order = findOrderById(orderId);
		order.setOrderStatus(OrderStatus.CONFIRMED);

		return orderRepository.save(order);
	}

	@Override
	public Order shippedOrder(Long orderId) throws OrderException {
		Order order = findOrderById(orderId);
		order.setOrderStatus(OrderStatus.SHIPPED);
		return orderRepository.save(order);
	}

	@Override
	public Order deliveredOrder(Long orderId) throws OrderException {
		Order order = findOrderById(orderId);
		order.setOrderStatus(OrderStatus.DELIVERED);
		return orderRepository.save(order);
	}

	@Override
	public Order cancledOrder(Long orderId) throws OrderException {
		Order order = findOrderById(orderId);
		order.setOrderStatus(OrderStatus.CANCELLED);
		return orderRepository.save(order);
	}

	@Override
	public Order findOrderById(Long orderId) throws OrderException {
		Optional<Order> opt = orderRepository.findById(orderId);

		if (opt.isPresent()) {
			return opt.get();
		}
		throw new OrderException("order not exist with id " + orderId);
	}

	@Override
	public List<Order> usersOrderHistory(Long userId) {
		List<Order> orders = orderRepository.getUsersOrders(userId);
		return orders;
	}

	@Override
	public List<Order> getAllOrders() {

		return orderRepository.findAllByOrderByCreatedAtDesc();
	}

	@Override
	public void deleteOrder(Long orderId) throws OrderException {
		Order order = findOrderById(orderId);

		orderRepository.deleteById(orderId);

	}

}