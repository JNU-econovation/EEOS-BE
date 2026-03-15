package com.blackcompany.eeos.announcement.presentation.docs;

import com.blackcompany.eeos.announcement.application.dto.SaveAnnouncementRequest;
import com.blackcompany.eeos.common.presentation.response.ApiResponse;
import com.blackcompany.eeos.common.presentation.response.ApiResponseBody.SuccessBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "공지사항 내부 API", description = "Slack 메시지를 수신하여 공지사항으로 저장하는 내부 API")
public interface AnnouncementInboundApi {

	@Operation(
			summary = "Slack 공지 메시지 수신",
			description = "Slack 공지 채널 메시지를 수신하여 공지사항으로 저장합니다. X-EEOS-API-KEY 헤더 인증이 필요합니다.")
	ApiResponse<SuccessBody<Void>> save(SaveAnnouncementRequest request);
}
