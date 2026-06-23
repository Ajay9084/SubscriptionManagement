package com.example.purchase_service.dto.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class ProductResponse {

	private Long id;

	private String name;

	private String description;

	private BigDecimal price;

	private String category;
}

