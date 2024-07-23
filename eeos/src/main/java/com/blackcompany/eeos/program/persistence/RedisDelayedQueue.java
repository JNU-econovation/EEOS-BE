package com.blackcompany.eeos.program.persistence;

import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisDelayedQueue {

	private final RedisTemplate<String, Object> redisTemplate;
	private final String QUEUE_KEY = "REDIS_QUEUE_KEY";

	public void addTask(Long programId, long delayInSeconds) {
		long executionTime = System.currentTimeMillis() + (delayInSeconds * 1000);
		redisTemplate.opsForZSet().add(QUEUE_KEY, programId, executionTime);
	}

	public Set<Long> getReadyTasks() {
		long now = System.currentTimeMillis();
		Set<Long> tasks =
				redisTemplate.opsForZSet().rangeByScore(QUEUE_KEY, 0, now).stream()
						.map(e -> (Long) e)
						.collect(Collectors.toSet());

		if (tasks != null && !tasks.isEmpty()) {
			redisTemplate.opsForZSet().removeRangeByScore(QUEUE_KEY, 0, now);
		}

		return tasks;
	}
}
