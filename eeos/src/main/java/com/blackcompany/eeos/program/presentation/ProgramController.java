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
import com.blackcompany.eeos.program.application.usecase.*;

import javax.validation.Valid;

import com.blackcompany.eeos.program.application.dto.ProgramSlackNotificationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "API", description = "행사에 관한 API")
public class ProgramController {

	private final CreateProgramUsecase createProgramUsecase;
	private final GetProgramUsecase getProgramUsecase;
	private final UpdateProgramUsecase updateProgramUsecase;
	private final GetProgramsUsecase getProgramsUsecase;
	private final DeleteProgramUsecase deleteProgramUsecase;
	private final GetAccessRightUsecase getAccessRightUsecase;
	private final NotifyProgramUsecase notifyProgramUsecase;


	@Operation(summary = "행사 생성", description = "RequestBody에 담긴 행사 정보를 통해서 행사를 생성한다.")
	@PostMapping
	public ApiResponse<SuccessBody<CommandProgramResponse>> create(
			@Member Long memberId, @RequestBody @Valid CreateProgramRequest request) {
		CommandProgramResponse response = createProgramUsecase.create(memberId, request);
		return ApiResponseGenerator.success(response, HttpStatus.CREATED, MessageCode.CREATE);
	}

	@Operation(summary = "행사 조회", description = "PathVariable에 담긴 programId를 통해서 행사 1기를 조회한다.")
	@GetMapping("/{programId}")
	public ApiResponse<SuccessBody<QueryProgramResponse>> findOne(
			@Member Long memberId, @PathVariable("programId") Long programId) {
		QueryProgramResponse response = getProgramUsecase.getProgram(memberId, programId);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.GET);
	}


	@Operation(summary = "행사 수정", description = "PathVariable에 담긴 programId를 사용해서 행사 1개를 삭제한다.")
	@PatchMapping("/{programId}")
	public ApiResponse<SuccessBody<CommandProgramResponse>> update(
			@Member Long memberId,
			@PathVariable("programId") Long programId,
			@RequestBody @Valid UpdateProgramRequest request) {
		CommandProgramResponse response = updateProgramUsecase.update(memberId, programId, request);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.UPDATE);
	}

	@Operation(summary = "행사 리스트 조회", description = "RequestParam 의 category, programStatus, size, page 를 사용해서 1 페이지에 들어가는 행사 리스트를 가져온다.")
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


	@Operation(summary = "행사 삭제", description = "PathVariable에 담긴 programId를 사용해서 행사를 삭제한다.")
	@DeleteMapping("/{programId}")
	public ApiResponse<SuccessBody<Void>> delete(
			@Member Long memberId, @PathVariable("programId") Long programId) {
		deleteProgramUsecase.delete(memberId, programId);
		return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.DELETE);
	}

	@Operation(summary = "행사 수정 권한", description = "PathVariable에 programId를 담아 사용자가 프로그램에 수정권한이 있는지 확인한다.")
	@GetMapping("/{programId}/accessRight")
	public ApiResponse<SuccessBody<QueryAccessRightResponse>> getAccessRight(
			@Member Long memberId, @PathVariable("programId") Long programId) {
		QueryAccessRightResponse response = getAccessRightUsecase.getAccessRight(memberId, programId);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.GET);
	}

	@PostMapping("/{programId}/slack/notification")
	public ApiResponse<SuccessBody<CommandProgramResponse>> slackNotify(
			@Member Long memberId, @RequestBody ProgramSlackNotificationRequest request,
			@PathVariable("programId") Long programId){
		CommandProgramResponse response = notifyProgramUsecase.notify(memberId, programId, request);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.CREATE); //프로그램 조회, 내용 형태를 바꾼다.
	}
}
