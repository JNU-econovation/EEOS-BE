package com.blackcompany.eeos.target.presentation.docs;

import com.blackcompany.eeos.common.presentation.respnose.ApiResponse;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseBody.SuccessBody;
import com.blackcompany.eeos.target.application.dto.AttendTeamBuildingRequest;
import com.blackcompany.eeos.target.application.dto.QueryTargetInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "팀빌딩 대상", description = "팀 빌딩 대상 관련 API")
public interface TeamBuildingTargetApi {

	@Operation(summary = "팀 빌딩 참여 생성", description = "팀빌딩에 참여한다.")
	ApiResponse<SuccessBody<Void>> create(
			@Parameter(hidden = true) Long memberId, AttendTeamBuildingRequest request);

	@Operation(summary = "팀 빌딩 참여 정보 업데이트", description = "팀빌딩에 참여한 정보를 변경한다.")
	ApiResponse<SuccessBody<Void>> update(
			@Parameter(hidden = true) Long memberId, AttendTeamBuildingRequest request);

	@Operation(summary = "대상 정보 조회", description = "팀빌딩 참여 대상 정보를 가져온다.")
	ApiResponse<SuccessBody<QueryTargetInfoResponse>> get(@Parameter(hidden = true) Long memberId);
}
