package com.example.purchase_service.service;

import com.example.purchase_service.dto.request.CreateProductRequest;
import com.example.purchase_service.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

	ProductResponse createProduct(CreateProductRequest request);

	ProductResponse getProductById(Long id);

	Page<ProductResponse> getAllProducts(Pageable pageable);

	ProductResponse updateProduct(Long id, CreateProductRequest request);


	void deleteProduct(Long id);

}
