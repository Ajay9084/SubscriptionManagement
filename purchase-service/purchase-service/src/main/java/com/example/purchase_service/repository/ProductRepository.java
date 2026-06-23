package com.example.purchase_service.repository;

import com.example.purchase_service.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
	boolean existsByName(String name);
}
