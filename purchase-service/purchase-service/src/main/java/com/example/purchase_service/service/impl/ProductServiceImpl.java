package com.example.purchase_service.service.impl;


import com.example.purchase_service.dto.request.CreateProductRequest;
import com.example.purchase_service.dto.response.ProductResponse;
import com.example.purchase_service.entity.Product;
import com.example.purchase_service.exception.ProductNotFoundException;
import com.example.purchase_service.mapper.ProductMapper;
import com.example.purchase_service.repository.ProductRepository;
import com.example.purchase_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;

	private final ProductMapper productMapper;

	@Override
	public ProductResponse createProduct(CreateProductRequest request){

		Product product = productMapper.toEntity(request);

		Product savedProduct = productRepository.save(product);

		return productMapper.toResponse(savedProduct);

	}


	@Override
	public ProductResponse getProductById(Long id){
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

		return productMapper.toResponse(product);
	}

	@Override
	public Page<ProductResponse> getAllProducts(Pageable pageable){
		return productRepository.findAll(pageable)
				.map(productMapper::toResponse);
	}


	@Override
	public ProductResponse updateProduct(Long id, CreateProductRequest request){
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ProductNotFoundException("Product not found with id:" + id));

		productMapper.updateEntity(product, request);

		Product updateProduct = productRepository.save(product);

		return productMapper.toResponse(updateProduct);
	}


	@Override
	public void deleteProduct(Long id){
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ProductNotFoundException("Product not found with id:" + id));

		productRepository.delete(product);
	}

}

