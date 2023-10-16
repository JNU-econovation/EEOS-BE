package com.blackcompany.eeos.program.presentation;

import com.blackcompany.eeos.common.presentation.respnose.ApiResponse;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseBody.SuccessBody;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseGenerator;
import com.blackcompany.eeos.common.presentation.respnose.MessageCode;
import com.blackcompany.eeos.program.application.dto.CommandProgramResponse;
import com.blackcompany.eeos.program.application.dto.CreateProgramRequest;
import com.blackcompany.eeos.program.application.dto.GetProgramResponse;
import com.blackcompany.eeos.program.application.dto.UpdateProgramRequest;
import com.blackcompany.eeos.program.application.usecase.CreateProgramUsecase;
import com.blackcompany.eeos.program.application.usecase.GetProgramUsecase;
import com.blackcompany.eeos.program.application.usecase.UpdateProgramUsecase;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/programs")
public class ProgramController {

	private final CreateProgramUsecase createBoardUsecase;
	private final GetProgramUsecase getProgramUsecase;
	private final UpdateProgramUsecase updateProgramUsecase;

	@PostMapping
	public ApiResponse<SuccessBody<CommandProgramResponse>> create(
			@RequestBody @Valid CreateProgramRequest request) {
		CommandProgramResponse response = createBoardUsecase.create(request);
		return ApiResponseGenerator.success(response, HttpStatus.CREATED, MessageCode.CREATE);
	}

	@GetMapping("/{programId}")
	public ApiResponse<SuccessBody<GetProgramResponse>> findOne(
			@PathVariable("programId") Long programId) {
		GetProgramResponse response = getProgramUsecase.getProgram(programId);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.GET);
	}

	@PutMapping("/{programId}")
	public ApiResponse<SuccessBody<CommandProgramResponse>> update(
			@PathVariable("programId") Long programId, @RequestBody @Valid UpdateProgramRequest request) {
		CommandProgramResponse response = updateProgramUsecase.update(programId, request);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.UPDATE);
	}
}
