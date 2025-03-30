package com.blackcompany.eeos.program.application.support;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DelayedQueue {

	private final RedisTemplate<String, Object> redisTemplate;

	public void addTask(String key, Object value, double score) {
		redisTemplate.opsForZSet().add(key, value, score);
	}

	public Set<Object> getReadyTasks(String key, double score) {
		Set<Object> tasks =
                redisTemplate.opsForZSet().rangeByScore(key, 0, score);

		if (tasks != null && !tasks.isEmpty()) {
			redisTemplate.opsForZSet().removeRangeByScore(key, 0, score);
		}

		return tasks;
	}
}
