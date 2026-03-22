package com.blackcompany.eeos.announcement.presentation.docs;

import com.blackcompany.eeos.announcement.application.dto.SaveAnnouncementRequest;
import com.blackcompany.eeos.common.presentation.response.ApiResponse;
import com.blackcompany.eeos.common.presentation.response.ApiResponseBody.SuccessBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "공지사항 내부 API", description = "Slack 메시지를 수신하여 공지사항으로 저장하는 내부 API")
public interface AnnouncementInboundApi {

	@Operation(
			summary = "Slack 공지 메시지 수신",
			description =
					"Slack 공지 채널 메시지를 수신하여 공지사항으로 저장합니다.\n\n"
							+ "- `X-EEOS-API-KEY` 헤더 인증이 필요합니다.\n"
							+ "- `threadTs`가 존재하는 답글 메시지는 저장하지 않습니다.\n"
							+ "- 동일한 `eventId`의 중복 요청은 무시합니다.\n"
							+ "- 메시지 본문은 Gemini AI로 파싱하며, 실패 시 rule-based 파서로 fallback합니다.")
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode = "201",
				description = "공지사항 저장 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode = "401",
				description = "유효하지 않은 내부 API 키",
				content = @Content)
	})
	ApiResponse<SuccessBody<Void>> save(
			@Parameter(hidden = true) @RequestBody SaveAnnouncementRequest request);
}
