//package com.example.purchase_service.config;
//
//
//import com.example.purchase_service.filter.JwtAuthenticationFilter;
//import com.example.purchase_service.security.CustomAccessDeniedHandler;
//import com.example.purchase_service.security.CustomUserDetailsService;
//import com.example.purchase_service.security.JwtAuthenticationEntryPoint;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableMethodSecurity
//@RequiredArgsConstructor
//public class SecurityConfig {
//
//	private final CustomUserDetailsService customUserDetailsService;
//	private final PasswordEncoder passwordEncoder;
//	private final JwtAuthenticationFilter jwtAuthenticationFilter;
//	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
//	private final CustomAccessDeniedHandler customAccessDeniedHandler;
//
//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//		http
//
//				// Disable CSRF because we are using JWT
//				.csrf(csrf -> csrf.disable())
//
//				// Stateless Session
//				.sessionManagement(session ->
//						session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//				)
//
//				// Authorization Rules
//				.authorizeHttpRequests(auth -> auth
//
//						// Public APIs
//						.requestMatchers("/auth/**").permitAll()
//
//						// Admin APIs
//						.requestMatchers("/api/admin/**").hasRole("ADMIN")
//
//						// Any other endpoint requires authentication
//						.anyRequest().authenticated()
//				)
//
//				// Exception Handling
//				.exceptionHandling(exception -> exception
//						.authenticationEntryPoint(jwtAuthenticationEntryPoint)
//						.accessDeniedHandler(customAccessDeniedHandler)
//				)
//
//				// Authentication Provider
//				.authenticationProvider(authenticationProvider())
//
//				// JWT Filter
//				.addFilterBefore(
//						jwtAuthenticationFilter,
//						UsernamePasswordAuthenticationFilter.class
//				);
//
//		return http.build();
//	}
//
//	@Bean
//	public AuthenticationProvider authenticationProvider() {
//
//		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//
//		provider.setUserDetailsService(customUserDetailsService);
//		provider.setPasswordEncoder(passwordEncoder);
//
//		return provider;
//	}
//
//	@Bean
//	public AuthenticationManager authenticationManager(
//			AuthenticationConfiguration configuration)
//			throws Exception {
//
//		return configuration.getAuthenticationManager();
//	}
//}
