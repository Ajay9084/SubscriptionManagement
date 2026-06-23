package com.example.subscription.specification;

import com.example.subscription.entity.Subscription;
import com.example.subscription.enums.SubscriptionStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class SubscriptionSpecification {



	public static Specification<Subscription> hasCustomerId(Long customerId) {
		return (root, query, cb) ->
				cb.equal(root.get("customerId"), customerId);
	}



	public static Specification<Subscription> hasProductId(Long productId) {
		return (root, query, cb) ->
				cb.equal(root.get("productId"), productId);
	}



	public static Specification<Subscription> hasStatus(SubscriptionStatus status) {
		return (root, query, cb) ->
				cb.equal(root.get("status"), status);
	}


	public static Specification<Subscription> hasStartDateBetween(
			LocalDate from,
			LocalDate to) {

		return (root, query, cb) ->
				cb.between(root.get("startDate"), from, to);
	}


	public static Specification<Subscription> hasExpiryDateBetween(
			LocalDate from,
			LocalDate to) {

		return (root, query, cb) ->
				cb.between(root.get("expiryDate"), from, to);
	}



	public static Specification<Subscription> isActive(Boolean active) {

		return (root, query, cb) -> {

			LocalDate today = LocalDate.now();

			if (active) {
				return cb.greaterThanOrEqualTo(
						root.get("expiryDate"),
						today
				);
			}

			return cb.lessThan(
					root.get("expiryDate"),
					today
			);
		};
	}
}