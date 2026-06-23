//package com.example.purchase_service.service.impl;
//
//
//import com.example.purchase_service.dto.request.LoginRequest;
//import com.example.purchase_service.dto.request.RegisterRequest;
//import com.example.purchase_service.dto.response.JwtResponse;
//import com.example.purchase_service.entity.Role;
//import com.example.purchase_service.entity.User;
//import com.example.purchase_service.repository.RoleRepository;
//import com.example.purchase_service.repository.UserRepository;
//import com.example.purchase_service.security.CustomUserDetails;
//import com.example.purchase_service.security.JwtService;
//import com.example.purchase_service.service.AuthService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class AuthServiceImpl implements AuthService {
//
//	private final UserRepository userRepository;
//	private final RoleRepository roleRepository;
//	private final PasswordEncoder passwordEncoder;
//	private final JwtService jwtService;
//	private final AuthenticationManager authenticationManager;
//
//	@Override
//	public String register(RegisterRequest request) {
//
//		log.info("Registration request received for username: {}", request.getUsername());
//
//		if (userRepository.existsByUsername(request.getUsername())) {
//			log.warn("Registration failed. Username '{}' already exists.", request.getUsername());
//			throw new RuntimeException("Username already exists");
//		}
//
//		if (userRepository.existsByEmail(request.getEmail())) {
//			log.warn("Registration failed. Email '{}' already exists.", request.getEmail());
//			throw new RuntimeException("Email already exists");
//		}
//
//		log.info("Fetching ROLE_USER from database.");
//
//		Role userRole = roleRepository.findByRoleName(RoleName.ROLE_USER)
//				.orElseThrow(() -> {
//					log.error("ROLE_USER not found in database.");
//					return new RuntimeException("Role not found");
//				});
//
//		User user = User.builder()
//				.username(request.getUsername())
//				.email(request.getEmail())
//				.password(passwordEncoder.encode(request.getPassword()))
//				.roles(Set.of(userRole))
//				.build();
//
//		log.info("Saving user '{}' into database.", user.getUsername());
//
//		userRepository.save(user);
//
//		log.info("User '{}' registered successfully.", user.getUsername());
//
//		return "User registered successfully";
//	}
//
//	@Override
//	public JwtResponse login(LoginRequest request) {
//
//		log.info("Login request received for username: {}", request.getUsername());
//
//		Authentication authentication = authenticationManager.authenticate(
//				new UsernamePasswordAuthenticationToken(
//						request.getUsername(),
//						request.getPassword()
//				)
//		);
//
//		log.info("User '{}' authenticated successfully.", request.getUsername());
//
//		CustomUserDetails userDetails =
//				(CustomUserDetails) authentication.getPrincipal();
//
//		log.info("Generating JWT token for user '{}'.", userDetails.getUsername());
//
//		String token = jwtService.generateToken(userDetails);
//
//		log.info("JWT token generated successfully for user '{}'.",
//				userDetails.getUsername());
//
//		return JwtResponse.builder()
//				.token(token)
//				.username(userDetails.getUsername())
//				.roles(
//						userDetails.getAuthorities()
//								.stream()
//								.map(authority -> authority.getAuthority())
//								.collect(Collectors.toSet())
//				)
//				.build();
//	}
//}