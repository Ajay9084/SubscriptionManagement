package com.example.purchase_service.repository;

import com.example.purchase_service.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {


	boolean existsByCustomerIdAndProduct_Id(Long customerId, Long productId);

//Optional<Purchase> findByCustomerIdAndProduct_Id(Long customerId, Long productId);
}
