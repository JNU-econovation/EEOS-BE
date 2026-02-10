package com.blackcompany.eeos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
@EnableRetry
public class RetryConfig {

	@Bean
	public RetryTemplate retryTemplate() {
		RetryTemplate template = new RetryTemplate();

		ExponentialBackOffPolicy backOff = new ExponentialBackOffPolicy();
		backOff.setInitialInterval(1000);
		backOff.setMultiplier(2.0);
		backOff.setMaxInterval(10000);
		template.setBackOffPolicy(backOff);

		SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
		retryPolicy.setMaxAttempts(4);
		template.setRetryPolicy(retryPolicy);

		return template;
	}
}
