package com.blackcompany.eeos.slackEvent.infra.repository;

import com.blackcompany.eeos.slackEvent.application.repository.SlackEventDedupRepository;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisSlackEventDedupRepository implements SlackEventDedupRepository {

	private static final long LOCK_TTL_SECONDS = 30L;
	private static final long PROCESSED_TTL_DAYS = 7L;

	private final RedisTemplate<String, Object> redisTemplate;

	// 이미 처리된 event인지 검증
	@Override
	public boolean isProcessed(String eventId) {
		return Boolean.TRUE.equals(redisTemplate.hasKey(processedKey(eventId)));
	}

	// 락 획득, 30초 유지
	@Override
	public boolean tryLock(String eventId) {
		Boolean acquired =
				redisTemplate
						.opsForValue()
						.setIfAbsent(lockKey(eventId), "1", LOCK_TTL_SECONDS, TimeUnit.SECONDS);
		return Boolean.TRUE.equals(acquired);
	}

	// 이벤트 처리됨 마킹, 7일 보관
	@Override
	public void markProcessed(String eventId) {
		redisTemplate.opsForValue().set(processedKey(eventId), "1", PROCESSED_TTL_DAYS, TimeUnit.DAYS);
	}

	@Override
	public void unlock(String eventId) {
		redisTemplate.delete(lockKey(eventId));
	}

	private String processedKey(String eventId) {
		return "slack:event:processed:" + eventId;
	}

	private String lockKey(String eventId) {
		return "slack:event:lock:" + eventId;
	}
}
