package com.example.purchase_service.service.impl;


import com.example.purchase_service.client.SubscriptionClient;
import com.example.purchase_service.dto.request.PurchaseRequest;
import com.example.purchase_service.dto.request.SubscriptionCreationRequest;
import com.example.purchase_service.dto.response.PurchaseResponse;
import com.example.purchase_service.entity.Product;
import com.example.purchase_service.entity.Purchase;
import com.example.purchase_service.exception.DuplicatePurchaseException;
import com.example.purchase_service.exception.ProductNotFoundException;
import com.example.purchase_service.mapper.PurchaseMapper;
import com.example.purchase_service.repository.ProductRepository;
import com.example.purchase_service.repository.PurchaseRepository;
import com.example.purchase_service.service.PurchaseService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class PurchaseServiceImpl implements PurchaseService {

	private final PurchaseRepository purchaseRepository;
	private final ProductRepository productRepository;
	private final PurchaseMapper purchaseMapper;
	private final SubscriptionClient subscriptionClient;

	@Override
	public PurchaseResponse createPurchase(PurchaseRequest request) {

		// Find Product
		Product product = productRepository.findById(request.getProductId())
				.orElseThrow(() ->
						new ProductNotFoundException(
								"Product not found with id: " + request.getProductId()));


		// Check duplicate purchase
		if (purchaseRepository.existsByCustomerIdAndProduct_Id(
				request.getCustomerId(),
				request.getProductId())) {

			throw new DuplicatePurchaseException(
					"Customer has already purchased this product.");
		}


		// Calculate Amount
		BigDecimal totalAmount = product.getPrice();

		// Create Purchase
		Purchase purchase = Purchase.builder()
				.customerId(request.getCustomerId())
				.product(product)
				.totalAmount(totalAmount)
				.paymentMethod(request.getPaymentMethod())
				.purchaseDate(LocalDateTime.now())
				.build();

// Save Purchase
		Purchase savedPurchase = purchaseRepository.save(purchase);

		try {
			subscriptionClient.createSubscription(
					new SubscriptionCreationRequest(
							request.getCustomerId(),
							request.getProductId()
					)
			);
		} catch (FeignException.Conflict ex) {
			throw new DuplicatePurchaseException(
					"Customer already has a subscription for this product."
			);
		}

		return purchaseMapper.toResponse(savedPurchase);

	}
}
