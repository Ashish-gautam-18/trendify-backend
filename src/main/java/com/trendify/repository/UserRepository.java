package com.trendify.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.trendify.entity.User;

// This interface manages database operations for registered Users
public interface UserRepository extends JpaRepository<User, Long> {
	
	// Finds a single user from the database using their unique email address
	public User findByEmail(String email);
	
	// Fetches a list of all users in the system, sorted with the newly registered users first
	public List<User> findAllByOrderByCreatedAtDesc();

}
