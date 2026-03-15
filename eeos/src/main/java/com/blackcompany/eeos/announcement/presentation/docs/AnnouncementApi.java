package com.blackcompany.eeos.announcement.presentation.docs;

import com.blackcompany.eeos.announcement.application.dto.GetAnnouncementsResponse;
import com.blackcompany.eeos.common.presentation.response.ApiResponse;
import com.blackcompany.eeos.common.presentation.response.ApiResponseBody.SuccessBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "공지사항", description = "공지사항 조회 API")
public interface AnnouncementApi {

	@Operation(summary = "공지사항 목록 조회", description = "저장된 공지사항 목록을 최신순으로 조회합니다.")
	ApiResponse<SuccessBody<GetAnnouncementsResponse>> getAnnouncements();
}
