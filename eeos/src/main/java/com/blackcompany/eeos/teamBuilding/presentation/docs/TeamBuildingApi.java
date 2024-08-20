package com.blackcompany.eeos.teamBuilding.presentation.docs;

import com.blackcompany.eeos.common.presentation.respnose.ApiResponse;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseBody.SuccessBody;
import com.blackcompany.eeos.teamBuilding.application.dto.CreateTeamBuildingRequest;
import com.blackcompany.eeos.teamBuilding.application.dto.ResultTeamBuildingResponse;
import com.blackcompany.eeos.teamBuilding.application.dto.ValidateTeamBuildingResponse;
import com.blackcompany.eeos.teamBuilding.application.usecase.QueryTeamBuildingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;

@Tag(name = "팀빌딩", description = "팀 빌딩 관련 API")
public interface TeamBuildingApi {

	@Operation(summary = "팀 빌딩 생성", description = "새로운 팀 빌딩을 생성합니다.")
	ApiResponse<SuccessBody<Void>> create(
			@Parameter(hidden = true) Long memberId, @Valid CreateTeamBuildingRequest request);

	@Operation(summary = "팀 빌딩 종료", description = "현재 진행 중인 팀 빌딩을 종료합니다.")
	ApiResponse<SuccessBody<Void>> end(@Parameter(hidden = true) Long memberId);

	@Operation(summary = "팀 빌딩 상태 검증", description = "특정 상태의 팀 빌딩이 존재하는지 검증합니다.")
	ApiResponse<SuccessBody<ValidateTeamBuildingResponse>> validateStatus(
			@Parameter(hidden = true) Long memberId, String status);

	@Operation(summary = "팀 빌딩 완료", description = "팀 빌딩 과정을 완료합니다.")
	ApiResponse<SuccessBody<Void>> complete(@Parameter(hidden = true) Long memberId);

	@Operation(summary = "팀 빌딩 결과 조회", description = "팀 빌딩 결과를 조회합니다.")
	ApiResponse<SuccessBody<ResultTeamBuildingResponse>> getResult(
			@Parameter(hidden = true) Long memberId);

	@Operation(summary = "팀 빌딩 정보 조회", description = "현재 진행 중인 팀 빌딩 정보를 조회합니다.")
	ApiResponse<SuccessBody<QueryTeamBuildingResponse>> getTeamBuilding(
			@Parameter(hidden = true) Long memberId);

	@Operation(summary = "팀 빌딩 삭제", description = "팀 빌딩을 삭제합니다.")
	ApiResponse<SuccessBody<Void>> deleteTeamBuilding(@Parameter(hidden = true) Long memberId);
}
