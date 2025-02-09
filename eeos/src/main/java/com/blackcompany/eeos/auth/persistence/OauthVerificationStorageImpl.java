package com.blackcompany.eeos.auth.persistence;

import com.blackcompany.eeos.auth.application.domain.OauthMemberModel;
import com.blackcompany.eeos.auth.application.repository.OauthVerificationStorage;
import com.blackcompany.eeos.auth.persistence.exception.ExpiredVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OauthVerificationStorageImpl implements OauthVerificationStorage {
	private final RedisTemplate<String, Object> redisTemplate;
	private static final String KEY_PREFIX = "oauth:verification:";
	private static final long EXPIRE_MINUTES = 30;

	@Override
	public UUID store(OauthMemberModel model) {
		OAuthInfo info =
				OAuthInfo.builder()
						.oauthId(model.getOauthId())
						.oauthServerType(model.getOauthServerType())
						.requiresAdditionalInfo(model.isRequiresAdditionalInfo())
						.build();

		UUID uuid = UUID.randomUUID();
		redisTemplate.opsForValue().set(getKey(uuid), info, EXPIRE_MINUTES, TimeUnit.MINUTES);
		return uuid;
	}

	@Override
	public OAuthInfo get(UUID uuid) {
		String key = getKey(uuid);
		Object value = redisTemplate.opsForValue().get(key);
		if (value == null) {
			throw new ExpiredVerificationException();
		}
		redisTemplate.delete(key);

		ObjectMapper mapper = new ObjectMapper();
		return mapper.convertValue(value, OAuthInfo.class);
	}

	private String getKey(UUID uuid) {
		return KEY_PREFIX + uuid;
	}
}
