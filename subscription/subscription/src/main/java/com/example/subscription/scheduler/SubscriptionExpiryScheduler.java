package com.example.subscription.scheduler;


import com.example.subscription.entity.Subscription;
import com.example.subscription.enums.SubscriptionStatus;
import com.example.subscription.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SubscriptionExpiryScheduler {

	private final SubscriptionRepository repository;

//    @Scheduled(cron = "0 0 0 * * *")
	@Scheduled(fixedRate = 30000)
	public void expiredSubscription(){

		List<Subscription> expiredSubscription = repository.findByStatusAndExpiryDateBefore(
				SubscriptionStatus.ACTIVE, LocalDate.now());

		for(Subscription subscription : expiredSubscription){
			subscription.setStatus(SubscriptionStatus.EXPIRED);

			repository.save(subscription);
		}
	}
}
