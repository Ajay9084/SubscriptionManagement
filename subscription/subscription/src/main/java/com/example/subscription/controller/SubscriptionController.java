package com.example.subscription.controller;

import com.example.subscription.dto.request.CreateSubscriptionRequest;
import com.example.subscription.dto.request.SubscriptionSearchFilter;
import com.example.subscription.dto.request.UpdateSubscriptionRequest;
import com.example.subscription.dto.response.SubscriptionResponse;
import com.example.subscription.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

	private final SubscriptionService service;


//	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<SubscriptionResponse> createSubscription(
			@Valid @RequestBody CreateSubscriptionRequest request) {

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(service.createSubscription(request));
	}

//	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	@GetMapping("/{id}")
	public ResponseEntity<SubscriptionResponse> getSubscriptionById(
			@PathVariable Long id) {

		return ResponseEntity.ok(service.getSubscriptionById(id));
	}


//	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	@GetMapping
	public ResponseEntity<Page<SubscriptionResponse>> getAllSubscriptions(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size,
			@RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "asc") String direction) {

		return ResponseEntity.ok(
				service.getAllSubscriptions(page, size, sortBy, direction)
		);
	}


//	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<SubscriptionResponse> updateSubscription(
			@PathVariable Long id,
			@Valid @RequestBody UpdateSubscriptionRequest request) {

		return ResponseEntity.ok(service.updateSubscription(id, request));
	}


//	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteSubscription(@PathVariable Long id) {

		service.deleteSubscription(id);
		return ResponseEntity.noContent().build();
	}


//	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/{id}/activate")
	public ResponseEntity<SubscriptionResponse> activateSubscription(
			@PathVariable Long id) {

		return ResponseEntity.ok(service.activateSubscription(id));
	}


//	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/{id}/suspend")
	public ResponseEntity<SubscriptionResponse> suspendSubscription(
			@PathVariable Long id) {

		return ResponseEntity.ok(service.suspendSubscription(id));
	}


//	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/{id}/resume")
	public ResponseEntity<SubscriptionResponse> resumeSubscription(
			@PathVariable Long id) {

		return ResponseEntity.ok(service.resumeSubscription(id));
	}


//	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/{id}/cancel")
	public ResponseEntity<SubscriptionResponse> cancelSubscription(
			@PathVariable Long id) {

		return ResponseEntity.ok(service.cancelSubscription(id));
	}


//	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	@GetMapping("/search")
	public ResponseEntity<Page<SubscriptionResponse>> subscriptionSearch(
			SubscriptionSearchFilter filter,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "createdAt") String sortBy,
			@RequestParam(defaultValue = "desc") String direction) {

		return ResponseEntity.ok(
				service.subscriptionSearch(filter, page, size, sortBy, direction)
		);
	}
}