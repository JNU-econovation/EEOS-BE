package com.blackcompany.eeos.program.presentation.docs;

import com.blackcompany.eeos.common.presentation.respnose.ApiResponse;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseBody.SuccessBody;
import com.blackcompany.eeos.program.application.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;

@Tag(name = "행사", description = "행사에 관한 API")
public interface ProgramApi {

	@Operation(summary = "행사 생성", description = "RequestBody에 담긴 행사 정보를 통해서 행사를 생성한다.")
	ApiResponse<SuccessBody<CommandProgramResponse>> create(
			@Parameter(hidden = true) Long memberId, @Valid CreateProgramRequest request);

	@Operation(summary = "행사 조회", description = "PathVariable에 담긴 programId를 통해서 행사 1기를 조회한다.")
	ApiResponse<SuccessBody<QueryProgramResponse>> findOne(
			@Parameter(hidden = true) Long memberId, Long programId);

	@Operation(summary = "행사 수정", description = "PathVariable에 담긴 programId를 사용해서 행사 1개를 삭제한다.")
	ApiResponse<SuccessBody<CommandProgramResponse>> update(
			@Parameter(hidden = true) Long memberId, Long programId, @Valid UpdateProgramRequest request);

	@Operation(
			summary = "행사 리스트 조회",
			description =
					"RequestParam 의 category, programStatus, size, page 를 사용해서 1 페이지에 들어가는 행사 리스트를 가져온다.")
	ApiResponse<SuccessBody<PageResponse<QueryProgramsResponse>>> findAll(
			String category, String status, int size, int page);

	@Operation(summary = "행사 삭제", description = "PathVariable에 담긴 programId를 사용해서 행사를 삭제한다.")
	ApiResponse<SuccessBody<Void>> delete(@Parameter(hidden = true) Long memberId, Long programId);

	@Operation(
			summary = "행사 수정 권한",
			description = "PathVariable에 programId를 담아 사용자가 프로그램에 수정권한이 있는지 확인한다.")
	ApiResponse<SuccessBody<QueryAccessRightResponse>> getAccessRight(
			@Parameter(hidden = true) Long memberId, Long programId);

	@Operation(
			summary = "행사 생성 자동 알림",
			description = "RequestBody에 programUrl을 담아 슬랙 API를 이용하여 슬랙봇 메세지 기능을 요청합니다.")
	ApiResponse<SuccessBody<CommandProgramResponse>> slackNotify(
			@Parameter(hidden = true) Long memberId,
			ProgramSlackNotificationRequest request,
			Long programId);

	@Operation(
			summary = "행사 출석 체크 시작",
			description = "PathVariable에 programId를 담아 해당 program의 출석 체크를 시작한다.")
	ApiResponse<SuccessBody<Void>> attend(
			@Parameter(hidden = true) Long memberId, Long programId, String mode);
}
