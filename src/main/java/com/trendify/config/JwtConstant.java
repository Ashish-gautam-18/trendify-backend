package com.trendify.config;

// This class contains constant values used for JWT token creation and validation
public class JwtConstant {
	
	// Secret key used to sign and verify the authenticity of JWT tokens
	public static final String SECRET_KEY="wpembytrwcvnryxksdbqwjebruyGHyudqgwveytrtrCSnwifoesarjbwe";
	
	// The HTTP Header name where the JWT token will be passed from frontend
	public static final String JWT_HEADER="Authorization";

}
