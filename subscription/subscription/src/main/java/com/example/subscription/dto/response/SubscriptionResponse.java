package com.example.subscription.dto.response;

import com.example.subscription.enums.SubscriptionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionResponse implements Serializable {

	private Long id;

	private Long customerId;

	private Long productId;

	private SubscriptionStatus status;

	private LocalDate startDate;

	private LocalDate expiryDate;

}
