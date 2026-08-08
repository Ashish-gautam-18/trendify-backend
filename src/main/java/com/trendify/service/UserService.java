package com.trendify.service;

import java.util.List;

import com.trendify.entity.User;
import com.trendify.exception.UserException;

// This interface defines the core business operations for managing user accounts and profiles
public interface UserService {
	
	// Finds and retrieves a specific user from the database using their unique ID
	User findUserById(Long userId) throws UserException;
	
	// Extracts information from a secure JWT token string to fetch the user's active profile
	User findUserProfileByJwt(String jwt) throws UserException;
	
	// Retrieves a complete list of all registered users present in the system
	List<User> findAllUsers();

}
