package com.blackcompany.eeos.auth.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** JWT 파싱 중 발생한 예외 */
public class TokenParsingException extends BusinessException {
	private static final String FAIL_CODE = "4007";

	public TokenParsingException(Throwable cause) {
		super(FAIL_CODE, HttpStatus.BAD_REQUEST, cause);
	}

	@Override
	public String getMessage() {
		Throwable cause = getCause();
		String causeMessage = cause != null ? cause.getMessage() : "Unknown error";
		return String.format("JWT 파싱 중 오류 발생: %s", causeMessage);
	}
}
