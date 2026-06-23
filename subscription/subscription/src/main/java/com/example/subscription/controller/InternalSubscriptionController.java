package com.example.subscription.controller;

import com.example.subscription.dto.request.SubscriptionCreationRequest;
import com.example.subscription.dto.response.SubscriptionResponse;
import com.example.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/subscriptions")
@RequiredArgsConstructor
public class InternalSubscriptionController {

	private final SubscriptionService subscriptionService;

	@PostMapping
	public ResponseEntity<SubscriptionResponse> createSubscription(
			@RequestBody SubscriptionCreationRequest request) {

		System.out.println("Internal Endpoint Hit ");

		SubscriptionResponse response =
				subscriptionService.createSubscriptionFromPurchase(
						request.getCustomerId(),
						request.getProductId());

		return ResponseEntity.ok(response);
	}
}
