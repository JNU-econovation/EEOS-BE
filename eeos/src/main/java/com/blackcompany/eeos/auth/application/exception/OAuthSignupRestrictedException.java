package com.blackcompany.eeos.auth.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class OAuthSignupRestrictedException extends BusinessException {

	private static final String FAIL_CODE = "4012";
	private final String oauthServerType;

	public OAuthSignupRestrictedException(String oauthServerType) {
		super(FAIL_CODE, HttpStatus.FORBIDDEN);
		this.oauthServerType = oauthServerType;
	}

	@Override
	public String getMessage() {
		return String.format("현재 %s 서비스를 통한 신규 가입이 제한되어 있습니다.", oauthServerType);
	}
}
