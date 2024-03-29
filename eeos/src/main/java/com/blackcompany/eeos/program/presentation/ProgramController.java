package com.blackcompany.eeos.program.presentation;

import com.blackcompany.eeos.auth.presentation.support.Member;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponse;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseBody.SuccessBody;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseGenerator;
import com.blackcompany.eeos.common.presentation.respnose.MessageCode;
import com.blackcompany.eeos.program.application.dto.CommandProgramResponse;
import com.blackcompany.eeos.program.application.dto.CreateProgramRequest;
import com.blackcompany.eeos.program.application.dto.PageResponse;
import com.blackcompany.eeos.program.application.dto.QueryAccessRightResponse;
import com.blackcompany.eeos.program.application.dto.QueryProgramResponse;
import com.blackcompany.eeos.program.application.dto.QueryProgramsResponse;
import com.blackcompany.eeos.program.application.dto.UpdateProgramRequest;
import com.blackcompany.eeos.program.application.usecase.CreateProgramUsecase;
import com.blackcompany.eeos.program.application.usecase.DeleteProgramUsecase;
import com.blackcompany.eeos.program.application.usecase.GetAccessRightUsecase;
import com.blackcompany.eeos.program.application.usecase.GetProgramUsecase;
import com.blackcompany.eeos.program.application.usecase.GetProgramsUsecase;
import com.blackcompany.eeos.program.application.usecase.UpdateProgramUsecase;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/programs")
public class ProgramController {

	private final CreateProgramUsecase createProgramUsecase;
	private final GetProgramUsecase getProgramUsecase;
	private final UpdateProgramUsecase updateProgramUsecase;
	private final GetProgramsUsecase getProgramsUsecase;
	private final DeleteProgramUsecase deleteProgramUsecase;
	private final GetAccessRightUsecase getAccessRightUsecase;

	@PostMapping
	public ApiResponse<SuccessBody<CommandProgramResponse>> create(
			@Member Long memberId, @RequestBody @Valid CreateProgramRequest request) {
		CommandProgramResponse response = createProgramUsecase.create(memberId, request);
		return ApiResponseGenerator.success(response, HttpStatus.CREATED, MessageCode.CREATE);
	}

	@GetMapping("/{programId}")
	public ApiResponse<SuccessBody<QueryProgramResponse>> findOne(
			@Member Long memberId, @PathVariable("programId") Long programId) {
		QueryProgramResponse response = getProgramUsecase.getProgram(memberId, programId);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.GET);
	}

	@PatchMapping("/{programId}")
	public ApiResponse<SuccessBody<CommandProgramResponse>> update(
			@Member Long memberId,
			@PathVariable("programId") Long programId,
			@RequestBody @Valid UpdateProgramRequest request) {
		CommandProgramResponse response = updateProgramUsecase.update(memberId, programId, request);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.UPDATE);
	}

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

	@DeleteMapping("/{programId}")
	public ApiResponse<SuccessBody<Void>> delete(
			@Member Long memberId, @PathVariable("programId") Long programId) {
		deleteProgramUsecase.delete(memberId, programId);
		return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.DELETE);
	}

	@GetMapping("/{programId}/accessRight")
	public ApiResponse<SuccessBody<QueryAccessRightResponse>> getAccessRight(
			@Member Long memberId, @PathVariable("programId") Long programId) {
		QueryAccessRightResponse response = getAccessRightUsecase.getAccessRight(memberId, programId);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.GET);
	}
}
