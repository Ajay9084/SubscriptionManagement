package com.example.purchase_service.client;

import com.example.purchase_service.dto.request.SubscriptionCreationRequest;
import com.example.purchase_service.dto.response.SubscriptionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
		name = "subscription-service",
		url = "${subscription.service.url}"
)
public interface SubscriptionClient {

	@PostMapping("/internal/subscriptions")
	SubscriptionResponse createSubscription(
			@RequestBody SubscriptionCreationRequest request
	);
}
