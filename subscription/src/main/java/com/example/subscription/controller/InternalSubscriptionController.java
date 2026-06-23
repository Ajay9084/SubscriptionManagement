package com.example.subscription.controller;

import com.example.subscription.dto.request.SubscriptionCreationRequest;
import com.example.subscription.dto.response.SubscriptionResponse;
import com.example.subscription.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/internal/subscriptions")
@RequiredArgsConstructor
@Tag(name = "Internal", description = "Internal endpoints — called by Purchase Service only, not intended for external use")
public class InternalSubscriptionController {

	private final SubscriptionService subscriptionService;

	@Operation(summary = "Create subscription from purchase event (internal use only)")
	@PostMapping
	public ResponseEntity<SubscriptionResponse> createSubscription(
			@Valid @RequestBody SubscriptionCreationRequest request) {

		log.info("Internal subscription creation request received for customerId={}, productId={}",
				request.getCustomerId(), request.getProductId());

		SubscriptionResponse response =
				subscriptionService.createSubscriptionFromPurchase(
						request.getCustomerId(),
						request.getProductId());

		return ResponseEntity.ok(response);
	}
}
