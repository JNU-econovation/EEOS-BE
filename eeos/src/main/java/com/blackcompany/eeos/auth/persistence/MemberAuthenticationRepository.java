package com.blackcompany.eeos.auth.persistence;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberAuthenticationRepository {

	private final RedisTemplate<String, Object> redisTemplate;

	public void save(String key, Long value, Long expiredTime) {
		redisTemplate.opsForValue().set(key, value, expiredTime, TimeUnit.MILLISECONDS);
	}

	public boolean isExistToken(String key) {
		return redisTemplate.hasKey(key);
	}
}
