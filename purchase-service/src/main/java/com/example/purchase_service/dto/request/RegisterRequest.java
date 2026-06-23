//package com.example.purchase_service.dto.request;
//
//import jakarta.persistence.Entity;
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.Size;
//import lombok.*;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class RegisterRequest {
//
//	@NotBlank(message = "Username is required")
//	private String username;
//
//	@Size(min = 6, message = "Password should be at least 6 characters")
//	private String password;
//
//	@Email(message = "Invalid Email")
//	@NotBlank(message = "Email is required")
//	private String email;
//
//}
//
