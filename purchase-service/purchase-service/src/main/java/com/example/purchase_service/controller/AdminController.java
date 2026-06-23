//package com.example.purchase_service.controller;
//
//import com.example.subscription.dto.request.RegisterRequest;
//import com.example.subscription.dto.response.UserResponse;
//import com.example.subscription.service.AdminService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/admin")
//@RequiredArgsConstructor
//public class AdminController {
//
//	private final AdminService adminService;
//
//	@PostMapping("/users")
//	@PreAuthorize("hasRole('ADMIN')")
//	public ResponseEntity<UserResponse> createAdmin(
//			@Valid @RequestBody RegisterRequest request) {
//
//		return ResponseEntity.status(HttpStatus.CREATED)
//				.body(adminService.createAdmin(request));
//	}
//}
