//package com.trendify.config;
//
//import java.util.Date;
//import javax.crypto.SecretKey;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Service;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//
//// This service class handles the creation and parsing of JWT tokens for Trendyfy application
//@Service
//public class JwtTokenProvider {
//	
//	// Generates a secure cryptographic key using the secret string from JwtConstant
//	private final SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
//	
//	// Generates a new JWT token valid for 24 hours when a user successfully logs in
//	public String generateToken(Authentication auth) {
//		
//		// 86400000 ms = 24 hours validity
//		return Jwts.builder()
//				.setIssuedAt(new Date())
//				.setExpiration(new Date(System.currentTimeMillis() + 86400000))
//				.claim("email", auth.getName())
//				.signWith(key)
//				.compact();
//	}
//	
//	// Extracts and decrypts the user's email address from an incoming JWT token string
//	public String getEmailFromJwtToken(String jwt) {
//		// Removes the "Bearer " prefix (7 characters) from the token string
//		if (jwt != null && jwt.startsWith("Bearer ")) {
//			jwt = jwt.substring(7);
//		}
//		
//		// Parses and reads the claims inside the verified JWT token safely using older valid syntax
//		Claims claims = Jwts.parserBuilder()
//				.setSigningKey(key)
//				.build()
//				.parseClaimsJws(jwt)
//				.getBody();
//		
//		return String.valueOf(claims.get("email"));
//	}
//}











package com.trendify.config;

import java.util.Date;
import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

// This service class handles the creation and parsing of JWT tokens for Trendyfy application
@Service
public class JwtTokenProvider {
	
	// Generates a secure cryptographic key using the secret string from JwtConstant
	private final SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
	
	// Generates a new JWT token valid for 24 hours when a user successfully logs in
	public String generateToken(Authentication auth) {
		
		// SECURITY FIX: User ke authorities (roles) ko comma-separated string me convert karke
		// token ke andar "authorities" claim me daal rahe hain, taaki JwtTokenValidator
		// request process karte waqt asli role pehchan sake, na ki hamesha default maan le.
		String authorities = auth.getAuthorities() == null || auth.getAuthorities().isEmpty()
				? "ROLE_CUSTOMER"
				: auth.getAuthorities().stream()
					.map(GrantedAuthority::getAuthority)
					.reduce((a, b) -> a + "," + b)
					.orElse("ROLE_CUSTOMER");
		
		// 86400000 ms = 24 hours validity
		return Jwts.builder()
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 86400000))
				.claim("email", auth.getName())
				.claim("authorities", authorities)
				.signWith(key)
				.compact();
	}
	
	// Extracts and decrypts the user's email address from an incoming JWT token string
	public String getEmailFromJwtToken(String jwt) {
		// Removes the "Bearer " prefix (7 characters) from the token string
		if (jwt != null && jwt.startsWith("Bearer ")) {
			jwt = jwt.substring(7);
		}
		
		// Parses and reads the claims inside the verified JWT token safely using older valid syntax
		Claims claims = Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(jwt)
				.getBody();
		
		return String.valueOf(claims.get("email"));
	}
}