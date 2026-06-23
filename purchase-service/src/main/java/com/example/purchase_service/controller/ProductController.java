package com.example.purchase_service.controller;



import com.example.purchase_service.dto.request.CreateProductRequest;
import com.example.purchase_service.dto.response.ProductResponse;
import com.example.purchase_service.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Product catalog management — CRUD operations")
public class ProductController {

	private final ProductService productService;

	@Operation(summary = "Create a new product")
	@PostMapping
	public ResponseEntity<ProductResponse> createProduct(
			@Valid @RequestBody CreateProductRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(productService.createProduct(request));
	}

	@Operation(summary = "Get product by ID")
	@GetMapping("/{id}")
	public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
		return ResponseEntity.ok(productService.getProductById(id));
	}

	@Operation(summary = "List all products (paginated)")
	@GetMapping
	public ResponseEntity<Page<ProductResponse>> getAllProducts(Pageable pageable) {
		return ResponseEntity.ok(productService.getAllProducts(pageable));
	}

	@Operation(summary = "Update a product")
	@PutMapping("/{id}")
	public ResponseEntity<ProductResponse> updateProduct(
			@PathVariable Long id,
			@Valid @RequestBody CreateProductRequest request) {
		return ResponseEntity.ok(productService.updateProduct(id, request));
	}

	@Operation(summary = "Delete a product")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
		productService.deleteProduct(id);
		return ResponseEntity.noContent().build();
	}
}

