package com.example.purchase_service.controller;



import com.example.purchase_service.dto.request.PurchaseRequest;
import com.example.purchase_service.dto.response.PurchaseResponse;
import com.example.purchase_service.service.PurchaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/purchase")
@RequiredArgsConstructor
public class PurchaseController {

	private final PurchaseService purchaseService;

	@PostMapping
	public ResponseEntity<PurchaseResponse> purchaseProduct(
			@Valid @RequestBody PurchaseRequest request){
		PurchaseResponse response = purchaseService.purchaseResponse(request);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(response);
	}
}
