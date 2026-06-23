package com.example.purchase_service.controller;



import com.example.purchase_service.dto.request.CreateProductRequest;
import com.example.purchase_service.dto.response.ProductResponse;
import com.example.purchase_service.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;


	@PostMapping
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ProductResponse> createProduct(
			@Valid @RequestBody CreateProductRequest request){
		ProductResponse response = productService.createProduct(request);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(response);
	}

	@GetMapping("/{id}")
//	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id){
		ProductResponse response = productService.getProductById(id);
		return ResponseEntity.ok(response);
	}


	@GetMapping
//	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<Page<ProductResponse>> getAllProducts(
			Pageable pageable) {

		return ResponseEntity.ok(productService.getAllProducts(pageable));
	}

	@PutMapping("/{id}")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ProductResponse> updateProduct(
			@PathVariable Long id,
			@Valid @RequestBody CreateProductRequest request){
		ProductResponse response = productService.updateProduct(id, request);

		return ResponseEntity.ok(response);
	}


	@DeleteMapping("/{id}")
//	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> deleteProduct(
			@PathVariable Long id){
		productService.deleteProduct(id);

		return ResponseEntity.noContent().build();
	}

}

