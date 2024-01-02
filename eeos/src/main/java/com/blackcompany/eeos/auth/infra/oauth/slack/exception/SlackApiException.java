package com.blackcompany.eeos.auth.infra.oauth.slack.exception;

import com.blackcompany.eeos.auth.infra.oauth.slack.dto.SlackApiResponse;
import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

/** slack API에서 실패 응답을 받을 시 발생하는 예외 */
public class SlackApiException extends BusinessException {

	private static final String FAIL_CODE = "5000";
	private final String apiMethod;
	private final SlackApiResponse slackApiResponse;

	public SlackApiException(final String apiMethod, final SlackApiResponse slackApiResponse) {
		super(FAIL_CODE, HttpStatus.BAD_REQUEST);
		this.apiMethod = apiMethod;
		this.slackApiResponse = slackApiResponse;
	}

	@Override
	public String getMessage() {
		return String.format(
				"슬랙 API 호출 실패 : %s api 호출 , %s로 응답", apiMethod, slackApiResponse.getError());
	}
}
