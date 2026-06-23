package com.example.subscription.service;

import com.example.subscription.dto.request.CreateSubscriptionRequest;
import com.example.subscription.dto.request.SubscriptionSearchFilter;
import com.example.subscription.dto.request.UpdateSubscriptionRequest;
import com.example.subscription.dto.response.SubscriptionResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SubscriptionService {

	SubscriptionResponse createSubscription(
			CreateSubscriptionRequest request);

	SubscriptionResponse getSubscriptionById(
			Long id);

	Page<SubscriptionResponse> getAllSubscriptions(
			int page,
			int size,
			String sortBy,
			String direction);

	SubscriptionResponse updateSubscription(
			Long id,
			UpdateSubscriptionRequest request);

	void deleteSubscription(
			Long id);



	SubscriptionResponse activateSubscription(Long id);

	SubscriptionResponse suspendSubscription(Long id);

	SubscriptionResponse resumeSubscription(Long id);

	SubscriptionResponse cancelSubscription(Long id);


	Page<SubscriptionResponse> subscriptionSearch(
			SubscriptionSearchFilter filter,
			int page,
			int size,
			String sortBy,
			String direction
	);

	SubscriptionResponse createSubscriptionFromPurchase(
			Long customerId,
			Long productId);
}
