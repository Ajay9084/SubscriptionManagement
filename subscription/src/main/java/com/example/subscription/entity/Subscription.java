package com.example.subscription.entity;

import com.example.subscription.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long customerId;

	@Column(nullable = false)
	private Long productId;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private SubscriptionStatus status;

	@Column(nullable = false)
	private LocalDate startDate;

	@Column(nullable = false)
	private LocalDate expiryDate;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;
}
