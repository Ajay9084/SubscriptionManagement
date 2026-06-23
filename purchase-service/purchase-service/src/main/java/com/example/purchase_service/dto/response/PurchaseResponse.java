package com.example.purchase_service.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PurchaseResponse {

	private Long purchaseId;

	private Long customerId;

	private Long productId;

	private String productName;

	private String category;

//private Integer quantity;

//private BigDecimal unitPrice;

	private BigDecimal totalAmount;

//private String shippingAddress;

	private LocalDateTime purchaseDate;
}
