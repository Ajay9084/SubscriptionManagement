//package com.example.purchase_service.security;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//
//import javax.crypto.SecretKey;
//import java.nio.charset.StandardCharsets;
//import java.util.Date;
//import java.util.function.Function;
//
//@Slf4j
//@Service
//public class JwtService {
//
//	@Value("${jwt.secret}")
//	private String secret;
//
//	@Value("${jwt.expiration}")
//	private long jwtExpiration;
//
//	// Generate JWT Token
//	public String generateToken(UserDetails userDetails) {
//
//		log.info("Generating JWT token for user: {}", userDetails.getUsername());
//
//		String token = Jwts.builder()
//				.subject(userDetails.getUsername())
//				.issuedAt(new Date())
//				.expiration(new Date(System.currentTimeMillis() + jwtExpiration))
//				.signWith(getSigningKey())
//				.compact();
//
//		log.info("JWT token generated successfully for user: {}", userDetails.getUsername());
//
//		return token;
//	}
//
//	// Extract Username
//	public String extractUsername(String token) {
//
//		String username = extractClaim(token, Claims::getSubject);
//
//		log.debug("Username extracted from JWT: {}", username);
//
//		return username;
//	}
//
//	// Validate Token
//	public boolean isTokenValid(String token, UserDetails userDetails) {
//
//		String username = extractUsername(token);
//
//		boolean valid = username.equals(userDetails.getUsername())
//				&& !isTokenExpired(token);
//
//		if (valid) {
//			log.info("JWT token validated successfully for user: {}", username);
//		} else {
//			log.warn("JWT token validation failed for user: {}", username);
//		}
//
//		return valid;
//	}
//
//	// Check Expiration
//	private boolean isTokenExpired(String token) {
//
//		boolean expired = extractExpiration(token).before(new Date());
//
//		if (expired) {
//			log.warn("JWT token has expired.");
//		}
//
//		return expired;
//	}
//
//	// Extract Expiration
//	private Date extractExpiration(String token) {
//		return extractClaim(token, Claims::getExpiration);
//	}
//
//	// Generic Claim Extractor
//	public <T> T extractClaim(String token, Function<Claims, T> resolver) {
//
//		Claims claims = extractAllClaims(token);
//
//		return resolver.apply(claims);
//	}
//
//	// Extract All Claims
//	private Claims extractAllClaims(String token) {
//
//		log.debug("Extracting claims from JWT.");
//
//		return Jwts.parser()
//				.verifyWith(getSigningKey())
//				.build()
//				.parseSignedClaims(token)
//				.getPayload();
//	}
//
//	// Secret Key
//	private SecretKey getSigningKey() {
//
//		log.debug("Loading JWT signing key.");
//
//		return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
//	}
//}