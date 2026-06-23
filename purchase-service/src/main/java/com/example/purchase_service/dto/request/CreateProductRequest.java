package com.example.purchase_service.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateProductRequest {

	@NotBlank(message = "Product name is required")
	private String name;


	private String description;

	@NotNull(message = "Price is required")
	@DecimalMin(value = "0.0", inclusive = false,
			message = "Price must be greater than 0")
	private BigDecimal price;

//	@NotNull(message = "Stock is required")
//	@PositiveOrZero(message = "Stock cannot be negative")
//	private Integer stock;

	@NotBlank(message = "Category is required")
	private String category;


}

