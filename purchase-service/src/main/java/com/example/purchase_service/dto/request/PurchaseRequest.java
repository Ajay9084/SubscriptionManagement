package com.example.purchase_service.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseRequest {

	@NotNull(message = "Customer Id is required")
	private Long customerId;


	@NotNull(message = "Product Id id required")
	private Long productId;

//	@NotNull(message = "Quantity is required")
//	@Min( value = 1, message = "Quantity must be at least 1")
//	private Integer quantity;

//	@NotBlank(message = "Shipping address is required")
//	private String shippingAddress;

	@NotBlank(message = "Payment method is required")
	private String paymentMethod;
}
