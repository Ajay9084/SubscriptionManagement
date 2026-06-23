//package com.example.purchase_service.service.impl;
//
//
//import com.example.purchase_service.dto.request.RegisterRequest;
//import com.example.purchase_service.dto.response.UserResponse;
//import com.example.purchase_service.entity.Role;
//import com.example.purchase_service.entity.User;
//import com.example.purchase_service.enums.RoleName;
//import com.example.purchase_service.repository.RoleRepository;
//import com.example.purchase_service.repository.UserRepository;
//import com.example.purchase_service.service.AdminService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class AdminServiceImpl implements AdminService {
//
//	private final UserRepository userRepository;
//	private final RoleRepository roleRepository;
//	private final PasswordEncoder passwordEncoder;
//
//	@Override
//	public UserResponse createAdmin(RegisterRequest request) {
//
//		log.info("Admin creation request received for username: {}", request.getUsername());
//
//		if (userRepository.existsByUsername(request.getUsername())) {
//			log.warn("Admin creation failed. Username '{}' already exists.",
//					request.getUsername());
//			throw new RuntimeException("Username already exists");
//		}
//
//		if (userRepository.existsByEmail(request.getEmail())) {
//			log.warn("Admin creation failed. Email '{}' already exists.",
//					request.getEmail());
//			throw new RuntimeException("Email already exists");
//		}
//
//		log.info("Fetching ROLE_ADMIN from database.");
//
//		Role adminRole = roleRepository.findByRoleName(RoleName.ROLE_ADMIN)
//				.orElseThrow(() -> {
//					log.error("ROLE_ADMIN not found in database.");
//					return new RuntimeException("ROLE_ADMIN not found");
//				});
//
//		User admin = User.builder()
//				.username(request.getUsername())
//				.email(request.getEmail())
//				.password(passwordEncoder.encode(request.getPassword()))
//				.roles(Set.of(adminRole))
//				.build();
//
//		log.info("Saving admin '{}' into database.", admin.getUsername());
//
//		User savedAdmin = userRepository.save(admin);
//
//		log.info("Admin '{}' created successfully with ID: {}",
//				savedAdmin.getUsername(),
//				savedAdmin.getId());
//
//		return UserResponse.builder()
//				.id(savedAdmin.getId())
//				.username(savedAdmin.getUsername())
//				.email(savedAdmin.getEmail())
//				.roles(
//						savedAdmin.getRoles()
//								.stream()
//								.map(role -> role.getRoleName().name())
//								.collect(Collectors.toSet())
//				)
//				.build();
//	}
//}
