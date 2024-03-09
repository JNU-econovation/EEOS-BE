package com.blackcompany.eeos.auth.persistence;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BlackAuthenticationRepository {

	private final RedisTemplate<String, Object> redisTemplate;

	public void save(String token, Long memberId, Long expiration) {
		redisTemplate.opsForValue().set(token, memberId, expiration, TimeUnit.MILLISECONDS);
	}

	public boolean isExistToken(String key) {
		return redisTemplate.hasKey(key);
	}
}
