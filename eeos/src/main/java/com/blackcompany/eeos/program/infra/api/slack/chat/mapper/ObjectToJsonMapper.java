package com.blackcompany.eeos.program.infra.api.slack.chat.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ObjectToJsonMapper<T> {

	private final ObjectMapper mapper = new ObjectMapper();

	public String toJson(T t) {
		try {
			return mapper.writeValueAsString(t);
		} catch (JsonProcessingException e) {
			log.error("JSON 변환 실패: {}", t, e);
			throw new IllegalStateException("JSON 변환에 실패했습니다.", e);
		}
	}
}
