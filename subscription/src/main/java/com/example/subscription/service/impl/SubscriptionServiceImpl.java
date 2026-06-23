package com.example.subscription.service.impl;


import com.example.subscription.dto.request.CreateSubscriptionRequest;
import com.example.subscription.dto.request.SubscriptionSearchFilter;
import com.example.subscription.dto.request.UpdateSubscriptionRequest;
import com.example.subscription.dto.response.SubscriptionResponse;
import com.example.subscription.entity.Subscription;
import com.example.subscription.enums.SubscriptionStatus;
import com.example.subscription.exception.DuplicateSubscriptionException;
import com.example.subscription.exception.InvalidStateTransitionException;
import com.example.subscription.exception.SubscriptionNotFoundException;
import com.example.subscription.mapper.SubscriptionMapper;

import com.example.subscription.repository.SubscriptionRepository;
import com.example.subscription.service.SubscriptionService;
import com.example.subscription.specification.SubscriptionSpecification;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
	private final SubscriptionRepository repository;
//	private final ProductRepository productRepository;
	private final SubscriptionMapper mapper;

	@Override
	@Transactional
	public SubscriptionResponse createSubscription(
			CreateSubscriptionRequest request){



		log.info(
				"Creating subscription for customer {} and product {}",
				request.getCustomerId(),
				request.getProductId());

		if(request.getExpiryDate()
				.isBefore(request.getStartDate())){
			throw new IllegalArgumentException(
					"Expiry Dates cannot be before Start date");
		}

		List<SubscriptionStatus> blockedStatuses =
				List.of(
						SubscriptionStatus.CREATED,
						SubscriptionStatus.ACTIVE,
						SubscriptionStatus.SUSPENDED
				);

		if(repository.existsByCustomerIdAndProductIdAndStatusIn(
				request.getCustomerId(),
				request.getProductId(),
				blockedStatuses)) {

			log.warn(
					"Duplicate subscription attempt for customer {} and product {}",
					request.getCustomerId(),
					request.getProductId());

			throw new DuplicateSubscriptionException(
					"Subscription already exists for this customer and product");
		}

		Subscription subscription =
				mapper.toEntity(request);

		Subscription saved =
				repository.save(subscription);

		log.info(
				"Subscription created successfully with id {}",
				saved.getId());

		return mapper.toResponse(saved);
	}





	@Override
	@Transactional
	public SubscriptionResponse createSubscriptionFromPurchase(
			Long customerId,
			Long productId) {

		List<SubscriptionStatus> blockedStatuses = List.of(
				SubscriptionStatus.CREATED,
				SubscriptionStatus.ACTIVE,
				SubscriptionStatus.SUSPENDED
		);

		if (repository.existsByCustomerIdAndProductIdAndStatusIn(
				customerId,
				productId,
				blockedStatuses)) {

			throw new DuplicateSubscriptionException(
					"Subscription already exists for this customer and product");
		}

		Subscription subscription = Subscription.builder()
				.customerId(customerId)
				.productId(productId)
				.status(SubscriptionStatus.CREATED)
				.startDate(LocalDate.now())
				.expiryDate(LocalDate.now().plusDays(30))
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();

		Subscription saved = repository.save(subscription);

		return mapper.toResponse(saved);
	}






	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "subscriptions", key = "#id")
	public SubscriptionResponse getSubscriptionById(Long id){

		log.info(
				"Fetching subscription with id {}",
				id);

		Subscription subscription = repository.findById(id)
				.orElseThrow(() ->
						new SubscriptionNotFoundException(
								"Subscription not found " + id));

		log.info(
				"Subscription {} fetched successfully",
				id);

		return mapper.toResponse(subscription);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<SubscriptionResponse> getAllSubscriptions(
			int page,
			int size,
			String sortBy,
			String direction){

		log.info(
				"Fetching subscriptions page={}, size={}, sortBy={}, direction={}",
				page,
				size,
				sortBy,
				direction);

		Sort sort = direction.equalsIgnoreCase("desc")
				? Sort.by(sortBy).descending()
				: Sort.by(sortBy).ascending();

		Pageable pageable =
				PageRequest.of(page, size, sort);

		Page<Subscription> subscriptions =
				repository.findAll(pageable);

		log.info(
				"Successfully fetched {} subscriptions",
				subscriptions.getNumberOfElements());

		return subscriptions.map(
				mapper::toResponse);
	}


	@Override
	@Transactional
	@CacheEvict(value = "subscriptions", key = "#id")
	public SubscriptionResponse updateSubscription(
			Long id,
			UpdateSubscriptionRequest request){

		log.info(
				"Updating subscription {}",
				id);

		Subscription subscription = repository.findById(id)
				.orElseThrow(() ->
						new SubscriptionNotFoundException(
								"Subscription Not Found for id - " + id));

		subscription.setExpiryDate(
				request.getExpiryDate());

		Subscription updated =
				repository.save(subscription);

		log.info(
				"Subscription {} updated successfully",
				id);

		return mapper.toResponse(updated);
	}


	@Override
	@Transactional
	@CacheEvict(value = "subscriptions", key = "#id")
	public void deleteSubscription(Long id){

		log.info(
				"Deleting subscription {}",
				id);

		Subscription subscription = repository.findById(id)
						.orElseThrow(() -> new SubscriptionNotFoundException("Subsctiption is already deleted for this id -> " + id));

		repository.deleteById(id);

		log.info(
				"Subscription {} deleted successfully",
				id);
	}


	@Override
	@Transactional
	@CacheEvict(value = "subscriptions", key = "#id")
	public SubscriptionResponse activateSubscription(Long id){

		Subscription subscription = repository.findById(id)
				.orElseThrow(() -> new SubscriptionNotFoundException("Subscription not found for id - " + id));

		if(subscription.getStatus() != SubscriptionStatus.CREATED){
			throw new InvalidStateTransitionException("Only CREATED subscription can be activated");
		}

		subscription.setStatus(SubscriptionStatus.ACTIVE);

		Subscription updated = repository.save(subscription);

		return mapper.toResponse(updated);
	}



	@Override
	@Transactional
	@CacheEvict(value = "subscriptions", key = "#id")
	public SubscriptionResponse suspendSubscription(Long id){
		Subscription subscription = repository.findById(id)
				.orElseThrow(() -> new SubscriptionNotFoundException("Subscription not found for id - " + id));

		if(subscription.getStatus() != SubscriptionStatus.ACTIVE){
			throw new InvalidStateTransitionException("Only ACTIVE subscription can be suspended");
		}
		subscription.setStatus(SubscriptionStatus.SUSPENDED);

		Subscription updated = repository.save(subscription);

		return mapper.toResponse(updated);
	}



	@Override
	@Transactional
	@CacheEvict(value = "subscriptions", key = "#id")
	public SubscriptionResponse resumeSubscription(Long id){
		Subscription subscription = repository.findById(id)
				.orElseThrow(() -> new SubscriptionNotFoundException("Subscription not found for id - " + id));

		if(subscription.getStatus() != SubscriptionStatus.SUSPENDED){
			throw new InvalidStateTransitionException("Only SUSPENDED subscription can be resumed");
		}

		subscription.setStatus(SubscriptionStatus.ACTIVE);

		Subscription updated = repository.save(subscription);

		return mapper.toResponse(updated);

	}



	@Override
	@Transactional
	@CacheEvict(value = "subscriptions", key = "#id")
	public SubscriptionResponse cancelSubscription(Long id){

		Subscription subscription = repository.findById(id)
				.orElseThrow(() -> new SubscriptionNotFoundException("Subscription not found for id - " + id));

		SubscriptionStatus status = subscription.getStatus();

		if(status != SubscriptionStatus.CREATED && status != SubscriptionStatus.ACTIVE && status != SubscriptionStatus.SUSPENDED){

			throw  new InvalidStateTransitionException("Only CREATED , ACTIVE or SUSPENDED subscription can be cancelled");
		}

		subscription.setStatus(SubscriptionStatus.CANCELLED);

		Subscription updated = repository.save(subscription);

		return mapper.toResponse(updated);

	}


	@Override
	@Transactional(readOnly = true)
	public Page<SubscriptionResponse> subscriptionSearch(
			SubscriptionSearchFilter filter,
			int page,
			int size,
			String sortBy,
			String direction) {

		log.info("Searching subscriptions with filter {}", filter);

		Sort sort = direction.equalsIgnoreCase("desc")
				? Sort.by(sortBy).descending()
				: Sort.by(sortBy).ascending();

		Pageable pageable = PageRequest.of(page, size, sort);

		Specification<Subscription> specification = Specification.where(null);

		if (filter.getCustomerId() != null) {
			specification = specification.and(
					SubscriptionSpecification.hasCustomerId(filter.getCustomerId())
			);
		}

		if (filter.getProductId() != null) {
			specification = specification.and(
					SubscriptionSpecification.hasProductId(filter.getProductId())
			);
		}

		if (filter.getStatus() != null) {
			specification = specification.and(
					SubscriptionSpecification.hasStatus(filter.getStatus())
			);
		}

		if (filter.getStartDateFrom() != null &&
				filter.getStartDateTo() != null) {

			specification = specification.and(
					SubscriptionSpecification.hasStartDateBetween(
							filter.getStartDateFrom(),
							filter.getStartDateTo()
					)
			);
		}

		if (filter.getExpiryDateFrom() != null &&
				filter.getExpiryDateTo() != null) {

			specification = specification.and(
					SubscriptionSpecification.hasExpiryDateBetween(
							filter.getExpiryDateFrom(),
							filter.getExpiryDateTo()
					)
			);
		}

		if (filter.getActive() != null) {
			specification = specification.and(
					SubscriptionSpecification.isActive(filter.getActive())
			);
		}

		Page<Subscription> subscriptions =
				repository.findAll(specification, pageable);

		log.info("Found {} subscriptions",
				subscriptions.getNumberOfElements());

		return subscriptions.map(mapper::toResponse);
	}


}
