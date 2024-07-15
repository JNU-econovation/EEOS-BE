package com.blackcompany.eeos.program.infra.api.slack.chat.mapper;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ObjectToJsonMapper<T> {

	private final ObjectMapper mapper = new ObjectMapper();

	/** 예외 처리 필요 */
	public String toJson(T t) {
		try {
			return mapper.writeValueAsString(t);
		} catch (JsonGenerationException e) {
			log.error(e.getMessage());
		} catch (JsonMappingException e) {
			log.error(e.getMessage());
		} catch (JsonProcessingException e) {
			log.error(e.getMessage());
		}

		return "hi";
	}
}
