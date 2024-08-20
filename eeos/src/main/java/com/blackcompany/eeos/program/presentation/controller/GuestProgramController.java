package com.blackcompany.eeos.program.presentation.controller;

import com.blackcompany.eeos.common.presentation.respnose.ApiResponse;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseBody.SuccessBody;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseGenerator;
import com.blackcompany.eeos.common.presentation.respnose.MessageCode;
import com.blackcompany.eeos.program.application.dto.PageResponse;
import com.blackcompany.eeos.program.application.dto.QueryProgramResponse;
import com.blackcompany.eeos.program.application.dto.QueryProgramsResponse;
import com.blackcompany.eeos.program.application.usecase.*;
import com.blackcompany.eeos.program.presentation.docs.GuestProgramApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/guest/programs")
public class GuestProgramController implements GuestProgramApi {
	private final GetProgramUsecase getProgramUsecase;
	private final GetProgramsUsecase getProgramsUsecase;

	@Override
	@GetMapping
	public ApiResponse<SuccessBody<PageResponse<QueryProgramsResponse>>> findAll(
			@RequestParam("category") String category,
			@RequestParam("programStatus") String status,
			@RequestParam("size") int size,
			@RequestParam("page") int page) {
		PageResponse<QueryProgramsResponse> response =
				getProgramsUsecase.getPrograms(category, status, size, page);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.GET);
	}

	@Override
	@GetMapping("/{programId}")
	public ApiResponse<SuccessBody<QueryProgramResponse>> findOne(
			@PathVariable("programId") Long programId) {
		QueryProgramResponse response = getProgramUsecase.getProgram(programId);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.GET);
	}
}
