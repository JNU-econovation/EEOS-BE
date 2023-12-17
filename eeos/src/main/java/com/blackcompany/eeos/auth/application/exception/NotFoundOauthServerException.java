package com.blackcompany.eeos.auth.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** 존재하지 않는 oauth 서버일 때 발생하는 예외 */
public class NotFoundOauthServerException extends BusinessException {
	private static final String FAIL_CODE = "5000";
	private final String oauthServer;

	public NotFoundOauthServerException(String oauthServer) {
		super(FAIL_CODE, HttpStatus.NOT_FOUND);
		this.oauthServer = oauthServer;
	}

	@Override
	public String getMessage() {
		return String.format("%s 는 존재하지 oauth 서버입니다.", oauthServer);
	}
}
