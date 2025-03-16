package com.blackcompany.eeos.target.application.model;

import com.blackcompany.eeos.target.application.exception.NotFoundSignTypeException;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum SignType {
	PLUS("plus"),
	MINUS("minus");

	private final String type;

	SignType(String type) {
		this.type = type;
	}

	public static SignType find(String type) {
		return Arrays.stream(SignType.values())
				.filter(value -> value.getType().equals(type))
				.findAny()
				.orElseThrow(() -> new NotFoundSignTypeException(type)); // TODO : 이거 변경하기
	}

	public static boolean isSame(String source, AttendStatus target) {
		return target.getStatus().equals(source);
	}
}
