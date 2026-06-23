package com.example.subscription.scheduler;

import com.example.subscription.entity.Subscription;
import com.example.subscription.enums.SubscriptionStatus;
import com.example.subscription.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriptionExpiryScheduler {

	private final SubscriptionRepository repository;

	/**
	 * Expires all ACTIVE subscriptions whose expiryDate has passed.
	 * Runs daily at midnight in production; set SUBSCRIPTION_EXPIRY_CRON env var to
	 * a shorter interval (e.g. "0/30 * * * * *") during local development.
	 */
	@Scheduled(cron = "${subscription.expiry.cron:0 0 0 * * *}")
	@Transactional
	public void expireSubscriptions() {
		List<Subscription> expired = repository.findByStatusAndExpiryDateBefore(
				SubscriptionStatus.ACTIVE, LocalDate.now());

		if (expired.isEmpty()) {
			return;
		}

		log.info("Expiring {} subscription(s)", expired.size());

		expired.forEach(s -> s.setStatus(SubscriptionStatus.EXPIRED));
		repository.saveAll(expired);

		log.info("Subscription expiry job completed");
	}
}
