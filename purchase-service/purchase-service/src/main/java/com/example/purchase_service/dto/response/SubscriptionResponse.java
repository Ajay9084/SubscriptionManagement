package com.example.purchase_service.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SubscriptionResponse {

	private Long id;
	private Long customerId;
	private Long productId;
	private String status;
	private LocalDate startDate;
	private LocalDate expiryDate;

}