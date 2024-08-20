package com.blackcompany.eeos.program.presentation.docs;

import com.blackcompany.eeos.common.presentation.respnose.ApiResponse;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseBody.SuccessBody;
import com.blackcompany.eeos.program.application.dto.PageResponse;
import com.blackcompany.eeos.program.application.dto.QueryProgramResponse;
import com.blackcompany.eeos.program.application.dto.QueryProgramsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "게스트모드 API", description = "게스트모드에 관한 API")
public interface GuestProgramApi {

	@Operation(
			summary = "게스트모드 - 행사 리스트 조회",
			description = "RequestParam에 담긴 category, programStatus, size, page를 이용해 프로그램 리스트를 조회한다.")
	ApiResponse<SuccessBody<PageResponse<QueryProgramsResponse>>> findAll(
			String category, String status, int size, int page);

	@Operation(
			summary = "행사 조회",
			description = "PathVariable에 담긴 prgramId를 이용해 특정 프로그램의 내용을 불러온다. 참석, 불참, 지각의 정보는 블라인트 처리된다.")
	ApiResponse<SuccessBody<QueryProgramResponse>> findOne(Long programId);
}
