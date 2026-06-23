package com.example.subscription.service;

import com.example.subscription.dto.request.CreateSubscriptionRequest;
import com.example.subscription.dto.response.SubscriptionResponse;
import com.example.subscription.entity.Subscription;
import com.example.subscription.enums.SubscriptionStatus;
import com.example.subscription.exception.DuplicateSubscriptionException;
import com.example.subscription.mapper.SubscriptionMapper;
import com.example.subscription.repository.SubscriptionRepository;
import com.example.subscription.service.impl.SubscriptionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceImplTest {

	@Mock
	private SubscriptionRepository repository;

	@Mock
	private SubscriptionMapper mapper;

	@InjectMocks
	private SubscriptionServiceImpl service;

	@Test
	void createSubscriptionSuccess() {

		CreateSubscriptionRequest request =
				new CreateSubscriptionRequest();

		request.setCustomerId(1L);
		request.setProductId(101L);
		request.setStartDate(LocalDate.now());
		request.setExpiryDate(LocalDate.now().plusDays(30));

		Subscription subscription =
				Subscription.builder()
						.customerId(1L)
						.productId(101L)
						.status(SubscriptionStatus.CREATED)
						.build();

		SubscriptionResponse response =
				SubscriptionResponse.builder()
						.id(1L)
						.customerId(1L)
						.productId(101L)
						.status(SubscriptionStatus.CREATED)
						.build();

		when(repository.existsByCustomerIdAndProductIdAndStatusIn(
				anyLong(),
				anyLong(),
				anyList()))
				.thenReturn(false);

		when(mapper.toEntity(request))
				.thenReturn(subscription);

		when(repository.save(subscription))
				.thenReturn(subscription);

		when(mapper.toResponse(subscription))
				.thenReturn(response);

		SubscriptionResponse result =
				service.createSubscription(request);

		assertNotNull(result);
		assertEquals(
				SubscriptionStatus.CREATED,
				result.getStatus());

		verify(repository).save(subscription);
	}

	@Test
	void createSubscriptionShouldThrowDuplicateException() {

		CreateSubscriptionRequest request =
				new CreateSubscriptionRequest();

		request.setCustomerId(1L);
		request.setProductId(101L);
		request.setStartDate(LocalDate.now());
		request.setExpiryDate(LocalDate.now().plusDays(30));

		when(repository.existsByCustomerIdAndProductIdAndStatusIn(
				anyLong(),
				anyLong(),
				anyList()))
				.thenReturn(true);

		assertThrows(
				DuplicateSubscriptionException.class,
				() -> service.createSubscription(request));
	}

	@Test
	void activateSubscriptionSuccess() {

		Subscription subscription =
				Subscription.builder()
						.id(1L)
						.status(SubscriptionStatus.CREATED)
						.build();

		SubscriptionResponse response =
				SubscriptionResponse.builder()
						.id(1L)
						.status(SubscriptionStatus.ACTIVE)
						.build();

		when(repository.findById(1L))
				.thenReturn(Optional.of(subscription));

		when(repository.save(subscription))
				.thenReturn(subscription);

		when(mapper.toResponse(subscription))
				.thenReturn(response);

		SubscriptionResponse result =
				service.activateSubscription(1L);

		assertEquals(
				SubscriptionStatus.ACTIVE,
				subscription.getStatus());

		verify(repository).save(subscription);
	}
}