package com.blackcompany.eeos.study.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionalService {
	private final RedisTemplate<String, String> redisTemplate;

	@Autowired
	public TransactionalService(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Transactional
	public Boolean checkKey(String key) {
		return redisTemplate.hasKey(key);
	}
}
