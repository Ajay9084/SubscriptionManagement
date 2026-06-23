package com.example.subscription.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateSubscriptionRequest {

	@NotNull(message = "Customer Id is required")
	private Long customerId;

	@NotNull(message = "Product Id is required")
	private Long productId;

	@NotNull(message = "Start Date is required")
	private LocalDate startDate;

	@NotNull(message = "Expiry Date is required")
	private LocalDate expiryDate;
}
