package com.example.purchase_service.mapper;


import com.example.purchase_service.dto.request.CreateProductRequest;
import com.example.purchase_service.dto.response.ProductResponse;
import com.example.purchase_service.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

	public Product toEntity(CreateProductRequest request) {
		return Product.builder()
				.name(request.getName())
				.description(request.getDescription())
				.price(request.getPrice())
				.category(request.getCategory())
				.build();
	}

	public ProductResponse toResponse(Product product) {
		return ProductResponse.builder()
				.id(product.getId())
				.name(product.getName())
				.description(product.getDescription())
				.price(product.getPrice())
				.category(product.getCategory())
				.build();
	}

	public void updateEntity(Product product, CreateProductRequest request) {
		product.setName(request.getName());
		product.setDescription(request.getDescription());
		product.setPrice(request.getPrice());
		product.setCategory(request.getCategory());
	}
}