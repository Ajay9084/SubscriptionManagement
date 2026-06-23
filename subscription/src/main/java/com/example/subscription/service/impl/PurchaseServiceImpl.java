//package com.example.subscription.service.impl;
//
//import com.example.subscription.dto.request.PurchaseRequest;
//import com.example.subscription.dto.response.PurchaseResponse;
//import com.example.subscription.entity.Product;
//import com.example.subscription.entity.Purchase;
//import com.example.subscription.enums.SubscriptionStatus;
//import com.example.subscription.exception.DuplicatePurchaseException;
//import com.example.subscription.exception.InsufficientStockException;
//import com.example.subscription.exception.ProductNotFoundException;
//import com.example.subscription.mapper.PurchaseMapper;
//import com.example.subscription.repository.ProductRepository;
//import com.example.subscription.repository.PurchaseRepository;
//import com.example.subscription.repository.SubscriptionRepository;
//import com.example.subscription.service.PurchaseService;
//import com.example.subscription.service.SubscriptionService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//@Transactional
//public class PurchaseServiceImpl implements PurchaseService {
//
//	private final PurchaseRepository purchaseRepository;
//	private final SubscriptionService subscriptionService;
//	private final SubscriptionRepository subscriptionRepository;
//	private final ProductRepository productRepository;
//
//	private final PurchaseMapper purchaseMapper;
//
//	@Override
//	public PurchaseResponse purchaseResponse(PurchaseRequest request){
//
//		Product product = productRepository.findById((request.getProductId()))
//				.orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + request.getProductId()));
//
//
////		if(product.getStock() < request.getQuantity()) {
////			throw new InsufficientStockException("Insufficient stock available");
////		}
//
////		product.setStock(product.getStock() - request.getQuantity());
//
//		BigDecimal totalAmount = product.getPrice();
////				.multiply(BigDecimal.valueOf(request.getQuantity()));
//
//
//
//		List<SubscriptionStatus> blockedStatues = List.of(
//				SubscriptionStatus.CREATED,
//				SubscriptionStatus.ACTIVE,
//				SubscriptionStatus.SUSPENDED
//		);
//
//		boolean subscriptionExists =
//				subscriptionRepository.existsByCustomerIdAndProductIdAndStatusIn(
//						request.getCustomerId(),
//						request.getProductId(),
//						blockedStatues);
//
//		if(subscriptionExists) {
//			throw new DuplicatePurchaseException(
//					"Customer already has an active subscription for this product");
//		}
//
//		Purchase purchase = Purchase.builder()
//				.customerId(request.getCustomerId())
//				.product(product)
////				.quantity(request.getQuantity())
////				.unitPrice(product.getPrice())
//				.totalAmount(totalAmount)
////				.shippingAddress(request.getShippingAddress())
//				.paymentMethod(request.getPaymentMethod())
//				.purchaseDate(LocalDateTime.now())
//				.build();
//
//
//
//		Purchase savedPurchase = purchaseRepository.save(purchase);
//
//		subscriptionService.createSubscriptionFromPurchase(
//				request.getCustomerId(),
//				request.getProductId()
//		);
//
//		return purchaseMapper.toResponse(savedPurchase);
//
//	}
//}






package com.example.subscription.service.impl;
