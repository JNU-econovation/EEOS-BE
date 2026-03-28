package com.blackcompany.eeos.auth.application.support;

import com.blackcompany.eeos.auth.application.exception.RateLimitExceededException;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoginRateLimiter {

	private static final String IP_KEY_PREFIX = "login_fail:ip:";
	private static final String ACCOUNT_KEY_PREFIX = "login_fail:account:";
	private static final int MAX_IP_ATTEMPTS_PER_MINUTE = 20;
	private static final int MAX_ACCOUNT_ATTEMPTS = 5;
	private static final long IP_WINDOW_SECONDS = 60;
	private static final long ACCOUNT_LOCKOUT_SECONDS = 300;

	private final RedisTemplate<String, Object> redisTemplate;

	public void checkRateLimit(String ip, String email) {
		try {
			checkIpLimit(ip);
			checkAccountLockout(email);
		} catch (RateLimitExceededException e) {
			throw e;
		} catch (Exception e) {
			log.warn("Rate limiter Redis error, allowing request: {}", e.getMessage());
		}
	}

	public void recordFailure(String ip, String email) {
		try {
			incrementCounter(IP_KEY_PREFIX + ip, IP_WINDOW_SECONDS);
			incrementCounter(ACCOUNT_KEY_PREFIX + email, ACCOUNT_LOCKOUT_SECONDS);
		} catch (Exception e) {
			log.warn("Failed to record login failure: {}", e.getMessage());
		}
	}

	public void resetAccountCounter(String email) {
		try {
			redisTemplate.delete(ACCOUNT_KEY_PREFIX + email);
		} catch (Exception e) {
			log.warn("Failed to reset account counter: {}", e.getMessage());
		}
	}

	private void checkIpLimit(String ip) {
		Long count = getCounter(IP_KEY_PREFIX + ip);
		if (count != null && count >= MAX_IP_ATTEMPTS_PER_MINUTE) {
			throw new RateLimitExceededException();
		}
	}

	private void checkAccountLockout(String email) {
		Long count = getCounter(ACCOUNT_KEY_PREFIX + email);
		if (count != null && count >= MAX_ACCOUNT_ATTEMPTS) {
			throw new RateLimitExceededException();
		}
	}

	private void incrementCounter(String key, long ttlSeconds) {
		Long count = redisTemplate.opsForValue().increment(key);
		if (count != null && count == 1) {
			redisTemplate.expire(key, ttlSeconds, TimeUnit.SECONDS);
		}
	}

	private Long getCounter(String key) {
		Object value = redisTemplate.opsForValue().get(key);
		if (value == null) {
			return null;
		}
		if (value instanceof Number) {
			return ((Number) value).longValue();
		}
		return Long.parseLong(value.toString());
	}
}
