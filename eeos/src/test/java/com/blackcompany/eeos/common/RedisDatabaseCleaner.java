package com.blackcompany.eeos.common;

import java.util.Set;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisDatabaseCleaner implements DatabaseCleaner {

	private final RedisTemplate<String, Object> redisTemplate;

	public RedisDatabaseCleaner(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public void clear() {
		Set<String> keys = redisTemplate.keys("*");

		if (keys != null && !keys.isEmpty()) {
			redisTemplate.delete(keys);
		}
	}
}
