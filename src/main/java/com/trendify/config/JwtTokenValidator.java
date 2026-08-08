package com.trendify.config;

import java.io.IOException;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// This filter intercepts every incoming HTTP request to validate the security JWT token
public class JwtTokenValidator extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		// Extracts the JWT token from the incoming Request Header configuration
		String jwt = request.getHeader(JwtConstant.JWT_HEADER);
		
		// Safe Null and Bearer Check to prevent string processing index errors
		if (jwt != null && jwt.startsWith("Bearer ")) {
			jwt = jwt.substring(7); // Removes the "Bearer " prefix text safely
			try {
				
				// Generates the cryptographic key to parse the token safely
				SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
				
				// Validates token and extracts internal user information claims
				Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
				
				String email = String.valueOf(claims.get("email"));
				
				String authorities = claims.get("authorities") != null ? String.valueOf(claims.get("authorities")) : "ROLE_CUSTOMER";
				
				List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
				
				Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, auths);
				
				SecurityContextHolder.getContext().setAuthentication(authentication);
				
			} catch (Exception e) {
				// SAFETY GUARD FIX: Instead of throwing a 500 server crash exception, we send a clear 401 Unauthorized code
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.setContentType("application/json");
				response.getWriter().write("{\"error\": \"Token has expired or is invalid. Please login again.\"}");
				return; // Stop the request filter chain processing immediately
			}
		}
		// Passes the request forward along the standard filter chain processing
		filterChain.doFilter(request, response);
	}
}
