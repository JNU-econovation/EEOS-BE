package com.blackcompany.eeos.study.redis;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

@SpringBootTest
@Tag("learning-test")
class RedisTransactionTest {

	@Autowired private RedisTemplate<String, String> redisTemplate;

	@Autowired private TransactionalService transactionalService;

	@BeforeEach
	void setup() {
		redisTemplate.opsForValue().set("testKey", "testValue");
	}

	@Test
	void redisTransactionInRedisTemplate() {
		boolean existsInTransactional = transactionalService.checkKey("testKey");
		assertTrue(existsInTransactional);
	}

	@Test
	void redisTransactionInSessionCallback() {
		redisTemplate.execute(
				new SessionCallback<Object>() {
					@Override
					public Object execute(RedisOperations operations) throws DataAccessException {
						operations.multi();
						Boolean beforeExec = operations.hasKey("testKey");
						assertNull(beforeExec);
						operations.exec();

						Boolean afterExec = operations.hasKey("testKey");
						assertTrue(afterExec);
						return null;
					}
				});
	}
}
