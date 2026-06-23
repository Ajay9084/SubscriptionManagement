package com.example.purchase_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "purchases")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Purchase {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long customerId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

//	@Column(nullable = false)
//	private Integer quantity;

//	@Column(nullable = false)
//	private BigDecimal unitPrice;

	@Column(nullable = false)
	private BigDecimal totalAmount;

//	@Column(nullable = false)
//	private String shippingAddress;

	@Column(nullable = false)
	private String paymentMethod;

	@Column(nullable = false)
	private LocalDateTime purchaseDate;
}