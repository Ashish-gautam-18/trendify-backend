//package com.trendify.config;
//
//import java.util.Arrays;
//import java.util.Collections;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//
//import jakarta.servlet.http.HttpServletRequest;
//
//// This class configures the global Spring Security and CORS settings for the application
//@Configuration
//public class AppConfig {
//	
//	// Main security chain bean that defines authorization rules for HTTP requests
//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		
//		http
//			// Configures session management to be entirely stateless using JWT tokens
//			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//			
//			// Defines authorization rules for specific API endpoint patterns
//			.authorizeHttpRequests(auth -> auth
//				// Allows all users to view products without logging in
//				.requestMatchers("/api/products/**").permitAll()
//				// Secures all other API endpoints so only authenticated users can access them
//				.requestMatchers("/api/**").authenticated()
//				// Allows any other general requests safely
//				.anyRequest().permitAll()
//			)
//			
//			// Attaches the custom JWT token validator filter before Basic Authentication check
//			.addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)
//			
//			// Disables CSRF protection since the application is stateless and uses JWT
//			.csrf(csrf -> csrf.disable())
//			
//			// Configures Cross-Origin Resource Sharing (CORS) to connect safely with frontend apps
//			.cors(cors -> cors.configurationSource(new CorsConfigurationSource() {
//				@Override
//				public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
//					CorsConfiguration cfg = new CorsConfiguration();
//					
//					// Defines the allowed origin URLs where the frontend applications run
//					cfg.setAllowedOrigins(Arrays.asList(
//						    "http://localhost:3000", 
//						    "http://localhost:4000",
//						    "http://localhost:4200",
//						    "https://trendify-frontend-ruddy.vercel.app"					
//					    ));
//					
//					// Allows all HTTP methods like GET, POST, PUT, DELETE
//					cfg.setAllowedMethods(Collections.singletonList("*"));
//					// Allows browser credentials like authentication headers or cookies
//					cfg.setAllowCredentials(true);
//					// Allows all custom headers from the frontend request
//					cfg.setAllowedHeaders(Collections.singletonList("*"));
//					// Exposes the Authorization header so frontend can read the JWT token
//					cfg.setExposedHeaders(Arrays.asList("Authorization"));
//					// Caches the CORS response configuration for 1 hour to increase speed
//					cfg.setMaxAge(3600L);
//					return cfg;
//				}
//			}))
//			
//			// Configures standard basic and form login features safely
//			.httpBasic(basic -> {})
//			.formLogin(form -> {});
//		
//		return http.build();
//	}
//	
//	// Bean to encode and verify user passwords securely using BCrypt hashing algorithm
//	// It is used during registration and login to keep passwords safe
//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
//}











package com.trendify.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

// This class configures the global Spring Security and CORS settings for the application
@Configuration
public class AppConfig {
	
	// Main security chain bean that defines authorization rules for HTTP requests
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http
			// Configures session management to be entirely stateless using JWT tokens
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			
			// Defines authorization rules for specific API endpoint patterns
			.authorizeHttpRequests(auth -> auth
				// Allows all users to view products without logging in
				.requestMatchers("/api/products/**").permitAll()
				// SECURITY FIX: Admin-only endpoints ab sirf ROLE_ADMIN authority wale
				// authenticated users hi access kar sakte hain. Ye rule sabse pehle
				// likhna zaroori hai (specific rule general rule se pehle check hoti hai).
				.requestMatchers("/api/admin/**").hasRole("ADMIN")
				// Secures all other API endpoints so only authenticated users can access them
				.requestMatchers("/api/**").authenticated()
				// Allows any other general requests safely
				.anyRequest().permitAll()
			)
			
			// Attaches the custom JWT token validator filter before Basic Authentication check
			.addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)
			
			// Disables CSRF protection since the application is stateless and uses JWT
			.csrf(csrf -> csrf.disable())
			
			// Configures Cross-Origin Resource Sharing (CORS) to connect safely with frontend apps
			.cors(cors -> cors.configurationSource(new CorsConfigurationSource() {
				@Override
				public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
					CorsConfiguration cfg = new CorsConfiguration();
					
					// Defines the allowed origin URLs where the frontend applications run
					cfg.setAllowedOrigins(Arrays.asList(
						    "http://localhost:3000", 
						    "http://localhost:4000",
						    "http://localhost:4200",
						    "https://trendify-frontend-ruddy.vercel.app"				
					    ));
					
					// Allows all HTTP methods like GET, POST, PUT, DELETE
					cfg.setAllowedMethods(Collections.singletonList("*"));
					// Allows browser credentials like authentication headers or cookies
					cfg.setAllowCredentials(true);
					// Allows all custom headers from the frontend request
					cfg.setAllowedHeaders(Collections.singletonList("*"));
					// Exposes the Authorization header so frontend can read the JWT token
					cfg.setExposedHeaders(Arrays.asList("Authorization"));
					// Caches the CORS response configuration for 1 hour to increase speed
					cfg.setMaxAge(3600L);
					return cfg;
				}
			}))
			
			// Configures standard basic and form login features safely
			.httpBasic(basic -> {})
			.formLogin(form -> {});
		
		return http.build();
	}
	
	// Bean to encode and verify user passwords securely using BCrypt hashing algorithm
	// It is used during registration and login to keep passwords safe
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}