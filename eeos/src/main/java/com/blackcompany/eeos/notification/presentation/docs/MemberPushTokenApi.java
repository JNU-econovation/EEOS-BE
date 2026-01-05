package com.blackcompany.eeos.notification.presentation.docs;

import com.blackcompany.eeos.common.presentation.response.ApiResponse;
import com.blackcompany.eeos.common.presentation.response.ApiResponseBody.SuccessBody;
import com.blackcompany.eeos.notification.application.dto.CreateMemberPushTokenRequest;
import com.blackcompany.eeos.notification.application.dto.DeleteMemberPushTokenRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "알림토큰", description = "알림 토큰 API")
public interface MemberPushTokenApi {

	@Operation(summary = "알림 토큰 등록", description = "사용자의 기기 알림 토큰을 등록한다.")
	ApiResponse<SuccessBody<Void>> create(
			@Parameter(hidden = true) Long memberId, @Valid CreateMemberPushTokenRequest request);

	@Operation(summary = "특정 기기 알림 토큰 삭제", description = "특정 기기 알림 토큰을 삭제한다.")
	ApiResponse<SuccessBody<Void>> delete(
			@Parameter(hidden = true) Long memberId, @Valid DeleteMemberPushTokenRequest request);

	@Operation(summary = "알림 토큰 전체 삭제", description = "특정 유저의 알림토큰을 모두 삭제한다.")
	ApiResponse<SuccessBody<Void>> deleteAll(@Parameter(hidden = true) Long memberId);
}
