package com.example.subscription.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateSubscriptionRequest {

	@NotNull(message = "Expiry date is required")
	@FutureOrPresent(message = "Expiry date must be today or in the future")
	private LocalDate expiryDate;
}
