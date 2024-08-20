package com.blackcompany.eeos.program.presentation.controller;

import com.blackcompany.eeos.auth.presentation.support.Member;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponse;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseBody.SuccessBody;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseGenerator;
import com.blackcompany.eeos.common.presentation.respnose.MessageCode;
import com.blackcompany.eeos.program.application.dto.*;
import com.blackcompany.eeos.program.application.usecase.*;
import com.blackcompany.eeos.program.presentation.docs.ProgramApi;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/programs")
public class ProgramController implements ProgramApi {

	private final CreateProgramUsecase createProgramUsecase;
	private final GetProgramUsecase getProgramUsecase;
	private final UpdateProgramUsecase updateProgramUsecase;
	private final GetProgramsUsecase getProgramsUsecase;
	private final DeleteProgramUsecase deleteProgramUsecase;
	private final GetAccessRightUsecase getAccessRightUsecase;
	private final NotifyProgramUsecase notifyProgramUsecase;
	private final AttendModeChangeUsecase attendStartUsecase;

	@Override
	@PostMapping
	public ApiResponse<SuccessBody<CommandProgramResponse>> create(
			@Member Long memberId, @RequestBody @Valid CreateProgramRequest request) {
		CommandProgramResponse response = createProgramUsecase.create(memberId, request);
		return ApiResponseGenerator.success(response, HttpStatus.CREATED, MessageCode.CREATE);
	}

	@Override
	@GetMapping("/{programId}")
	public ApiResponse<SuccessBody<QueryProgramResponse>> findOne(
			@Member Long memberId, @PathVariable("programId") Long programId) {
		QueryProgramResponse response = getProgramUsecase.getProgram(memberId, programId);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.GET);
	}

	@Override
	@PatchMapping("/{programId}")
	public ApiResponse<SuccessBody<CommandProgramResponse>> update(
			@Member Long memberId,
			@PathVariable("programId") Long programId,
			@RequestBody @Valid UpdateProgramRequest request) {
		CommandProgramResponse response = updateProgramUsecase.update(memberId, programId, request);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.UPDATE);
	}

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
	@DeleteMapping("/{programId}")
	public ApiResponse<SuccessBody<Void>> delete(
			@Member Long memberId, @PathVariable("programId") Long programId) {
		deleteProgramUsecase.delete(memberId, programId);
		return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.DELETE);
	}

	@Override
	@GetMapping("/{programId}/accessRight")
	public ApiResponse<SuccessBody<QueryAccessRightResponse>> getAccessRight(
			@Member Long memberId, @PathVariable("programId") Long programId) {
		QueryAccessRightResponse response = getAccessRightUsecase.getAccessRight(memberId, programId);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.GET);
	}

	@Override
	@PostMapping("/{programId}/slack/notification")
	public ApiResponse<SuccessBody<CommandProgramResponse>> slackNotify(
			@Member Long memberId,
			@RequestBody ProgramSlackNotificationRequest request,
			@PathVariable("programId") Long programId) {
		CommandProgramResponse response = notifyProgramUsecase.notify(memberId, programId, request);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.CREATE);
	}

	@Override
	@PostMapping("/{programId}")
	public ApiResponse<SuccessBody<Void>> attend(
			@Member Long memberId,
			@PathVariable("programId") Long programId,
			@RequestParam("mode") String mode) {
		attendStartUsecase.changeMode(memberId, programId, mode);
		return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.UPDATE);
	}
}
