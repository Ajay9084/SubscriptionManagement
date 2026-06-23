//package com.example.purchase_service.filter;
//
//
//
//import com.example.purchase_service.security.CustomUserDetailsService;
//import com.example.purchase_service.security.JwtService;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//@RequiredArgsConstructor
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//	private final JwtService jwtService;
//	private final CustomUserDetailsService userDetailsService;
//
//	@Override
//	protected void doFilterInternal(
//			HttpServletRequest request,
//			HttpServletResponse response,
//			FilterChain filterChain
//	) throws ServletException, IOException {
//
//		//  Get Authorization Header
//		String authHeader = request.getHeader("Authorization");
//
//		String token = null;
//		String username = null;
//
//		// Check if header contains Bearer token
//		if (authHeader != null && authHeader.startsWith("Bearer ")) {
//			token = authHeader.substring(7);
//			username = jwtService.extractUsername(token);
//		}
//
//		//  If username exists and user is not already authenticated
//		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//
//			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//
//			//  Validate token
//			if (jwtService.isTokenValid(token, userDetails)) {
//
//				//  Create Authentication object
//				UsernamePasswordAuthenticationToken authToken =
//						new UsernamePasswordAuthenticationToken(
//								userDetails,
//								null,
//								userDetails.getAuthorities()
//						);
//
//				authToken.setDetails(
//						new WebAuthenticationDetailsSource().buildDetails(request)
//				);
//
//				//  Set authentication in Security Context
//				SecurityContextHolder.getContext().setAuthentication(authToken);
//			}
//		}
//
//		//  Continue filter chain
//		filterChain.doFilter(request, response);
//	}
//}
