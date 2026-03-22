package com.blackcompany.eeos.announcement.presentation.docs;

import com.blackcompany.eeos.announcement.application.dto.GetAnnouncementsResponse;
import com.blackcompany.eeos.common.presentation.response.ApiResponse;
import com.blackcompany.eeos.common.presentation.response.ApiResponseBody.SuccessBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "공지사항", description = "공지사항 조회 API")
public interface AnnouncementApi {

	@Operation(
			summary = "공지사항 목록 조회",
			description = "Slack 공지 채널에서 수신된 공지사항 목록을 최신순으로 조회합니다.",
			security = @SecurityRequirement(name = "bearerAuth"))
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode = "200",
				description = "공지사항 목록 조회 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode = "401",
				description = "2000: 인증이 필요합니다 (JWT 토큰 없음 또는 만료)",
				content = @Content)
	})
	ApiResponse<SuccessBody<GetAnnouncementsResponse>> getAnnouncements();
}
