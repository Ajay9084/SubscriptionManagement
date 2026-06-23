package com.example.purchase_service.controller;



import com.example.purchase_service.dto.request.PurchaseRequest;
import com.example.purchase_service.dto.response.PurchaseResponse;
import com.example.purchase_service.service.PurchaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/purchase")
@RequiredArgsConstructor
@Tag(name = "Purchases", description = "Purchase a product — triggers automatic subscription creation in the Subscription Service")
public class PurchaseController {

	private final PurchaseService purchaseService;

	@Operation(summary = "Purchase a product and auto-create a subscription")
	@PostMapping
	public ResponseEntity<PurchaseResponse> purchaseProduct(
			@Valid @RequestBody PurchaseRequest request) {

		log.info("Purchase request received for customerId={}, productId={}",
				request.getCustomerId(), request.getProductId());

		PurchaseResponse response = purchaseService.createPurchase(request);

		log.info("Purchase completed successfully for customerId={}", request.getCustomerId());

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
