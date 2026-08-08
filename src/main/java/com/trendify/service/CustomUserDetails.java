
package com.trendify.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.trendify.entity.User;
import com.trendify.repository.UserRepository;

// This service class integrates with Spring Security to load user details from the database during authentication
@Service
public class CustomUserDetails implements UserDetailsService {
	
	private UserRepository userRepository;
	
	// Constructor injection to safely link the user database operations
	public CustomUserDetails(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	// Fetches the user by their email string and builds a secure UserDetails object for Spring Security
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepository.findByEmail(username);
		
		// If no registered user matches the incoming email, authentication fails immediately
		if (user == null) {
			throw new UsernameNotFoundException("user not found with email " + username);
		}
		
		List<GrantedAuthority> authorities = new ArrayList<>();
		
		// Returns Spring Security's own User object containing the authenticated credentials
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
	}

}

