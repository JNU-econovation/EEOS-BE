package com.blackcompany.eeos.notification.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotFoundNotificationProviderException extends BusinessException {

	private static final String FAIL_CODE = "5006";
	private final String provider;

	public NotFoundNotificationProviderException(String provider) {
		super(FAIL_CODE, HttpStatus.NOT_FOUND);
		this.provider = provider;
	}

	@Override
	public String getMessage() {
		return String.format("%s 는 존재하지 않는 외부 제공자 입니다.", provider);
	}
}
