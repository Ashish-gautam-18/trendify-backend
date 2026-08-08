package com.trendify.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.trendify.config.JwtTokenProvider;
import com.trendify.entity.User;
import com.trendify.exception.UserException;
import com.trendify.repository.UserRepository;

// This service class implements the business logic for managing user profiles and fetching user records
@Service
public class UserServiceImplementation implements UserService {
	
	private UserRepository userRepository;
	private JwtTokenProvider jwtTokenProvider;
	
	// Constructor injection to safely link the database repository and JWT token utility helper
	public UserServiceImplementation(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
		this.userRepository = userRepository;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	// Searches for a user profile using their unique ID and returns it, or throws a UserException
	@Override
	public User findUserById(Long userId) throws UserException {
		Optional<User> user = userRepository.findById(userId);
		
		if (user.isPresent()){
			return user.get();
		}
		throw new UserException("user not found with id " + userId);
	}

	// Extracts the email string from a security JWT token to verify and load the corresponding user profile
	@Override
	public User findUserProfileByJwt(String jwt) throws UserException {
		System.out.println("user service");
		String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
		
		System.out.println("email" + email);
		
		User user = userRepository.findByEmail(email);
		
		if (user == null) {
			throw new UserException("user not exist with email " + email);
		}
		System.out.println("email user" + user.getEmail());
		return user;
	}

	// Retrieves a complete list of all users from the database sorted by their profile creation timestamp
	@Override
	public List<User> findAllUsers() {
		return userRepository.findAllByOrderByCreatedAtDesc();
	}

}
