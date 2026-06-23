package com.example.purchase_service.mapper;


import com.example.purchase_service.dto.response.PurchaseResponse;
import com.example.purchase_service.entity.Purchase;
import org.springframework.stereotype.Component;

@Component
public class PurchaseMapper {

	public PurchaseResponse toResponse(Purchase purchase){
		return PurchaseResponse.builder()
				.purchaseId(purchase.getId())
				.customerId(purchase.getCustomerId())
				.productId(purchase.getProduct().getId())
				.productName(purchase.getProduct().getName())
				.category(purchase.getProduct().getCategory())
//				.quantity(purchase.getQuantity())
//				.unitPrice(purchase.getUnitPrice())
				.totalAmount(purchase.getTotalAmount())
//				.shippingAddress(purchase.getShippingAddress())
				.purchaseDate(purchase.getPurchaseDate())
				.build();
	}
}
