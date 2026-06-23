package com.example.subscription.dto.request;

import com.example.subscription.enums.SubscriptionStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SubscriptionSearchFilter {

	private Long customerId;

	private Long productId;

	private SubscriptionStatus status;

	// Search by Subscription Start Date Range
	private LocalDate startDateFrom;
	private LocalDate startDateTo;

	// Search by Expiry Date Range
	private LocalDate expiryDateFrom;
	private LocalDate expiryDateTo;

	// Active / Inactive Subscription
	private Boolean active;
}
