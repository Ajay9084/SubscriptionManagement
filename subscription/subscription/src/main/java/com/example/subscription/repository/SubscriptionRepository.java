package com.example.subscription.repository;

import com.example.subscription.entity.Subscription;
import com.example.subscription.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;

public interface SubscriptionRepository
		extends JpaRepository<Subscription, Long>, JpaSpecificationExecutor<Subscription> {

	boolean existsByCustomerIdAndProductIdAndStatusIn(
			Long customerId,
			Long productId,
			List<SubscriptionStatus> statuses);

	List<Subscription> findByStatusAndExpiryDateBefore(
			SubscriptionStatus status,
			LocalDate date);


	List<Subscription> findByStatusAndCustomerId(SubscriptionStatus status, Long customerId);

}