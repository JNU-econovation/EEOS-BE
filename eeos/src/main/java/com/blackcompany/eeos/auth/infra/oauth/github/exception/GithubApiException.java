package com.blackcompany.eeos.auth.infra.oauth.github.exception;

import com.blackcompany.eeos.auth.infra.oauth.github.dto.GithubErrorResponse;
import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** github API에서 실패 응답을 받을 시 발생하는 예외 */
public class GithubApiException extends BusinessException {
	private static final String FAIL_CODE = "5001";
	private final GithubErrorResponse errorResponse;

	public GithubApiException(GithubErrorResponse errorResponse) {
		super(FAIL_CODE, HttpStatus.BAD_REQUEST);
		this.errorResponse = errorResponse;
	}

	@Override
	public String getMessage() {
		return errorResponse.getFormattedMessage();
	}
}
