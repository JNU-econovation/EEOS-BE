package com.blackcompany.eeos.auth.persistence;

import com.blackcompany.eeos.auth.application.domain.AuthorizationCodeData;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AuthorizationCodeRepository {

	private static final String KEY_PREFIX = "auth_code:";
	private static final long TTL_SECONDS = 60;
	private static final int CODE_BYTE_LENGTH = 16;

	private final RedisTemplate<String, Object> redisTemplate;
	private final ObjectMapper objectMapper;

	public String save(AuthorizationCodeData data) {
		String code = generateCode();
		redisTemplate.opsForValue().set(KEY_PREFIX + code, data, TTL_SECONDS, TimeUnit.SECONDS);
		return code;
	}

	public Optional<AuthorizationCodeData> findAndDelete(String code) {
		String key = KEY_PREFIX + code;
		Object value = redisTemplate.opsForValue().get(key);
		if (value == null) {
			return Optional.empty();
		}
		redisTemplate.delete(key);
		return Optional.of(objectMapper.convertValue(value, AuthorizationCodeData.class));
	}

	private String generateCode() {
		byte[] bytes = new byte[CODE_BYTE_LENGTH];
		new SecureRandom().nextBytes(bytes);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
	}
}
