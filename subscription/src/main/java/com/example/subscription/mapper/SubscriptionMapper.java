package com.example.subscription.mapper;

import com.example.subscription.dto.request.CreateSubscriptionRequest;
import com.example.subscription.dto.response.SubscriptionResponse;
import com.example.subscription.entity.Subscription;
import com.example.subscription.enums.SubscriptionStatus;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionMapper {

	public Subscription toEntity(CreateSubscriptionRequest dto){
			return Subscription.builder()
				.customerId(dto.getCustomerId())
				.productId(dto.getProductId())
				.startDate(dto.getStartDate())
				.expiryDate(dto.getExpiryDate())
				.status(SubscriptionStatus.CREATED)
				.build();
	}

	public SubscriptionResponse toResponse(Subscription subscription){
		return  SubscriptionResponse.builder()
				.id(subscription.getId())
				.customerId(subscription.getCustomerId())
				.productId(subscription.getProductId())
				.status(subscription.getStatus())
				.startDate(subscription.getStartDate())
				.expiryDate(subscription.getExpiryDate())
				.build();
	}


}
