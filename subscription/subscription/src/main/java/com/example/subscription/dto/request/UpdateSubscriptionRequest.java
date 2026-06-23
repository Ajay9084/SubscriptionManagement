package com.example.subscription.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateSubscriptionRequest {

	@NotNull
private LocalDate expiryDate;
}
