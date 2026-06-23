package com.example.subscription.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateSubscriptionRequest {

	@NotNull(message = "Customer ID is required")
	private Long customerId;

	@NotNull(message = "Product ID is required")
	private Long productId;

	@NotNull(message = "Start date is required")
	@FutureOrPresent(message = "Start date must be today or in the future")
	private LocalDate startDate;

	@NotNull(message = "Expiry date is required")
	@FutureOrPresent(message = "Expiry date must be today or in the future")
	private LocalDate expiryDate;
}
