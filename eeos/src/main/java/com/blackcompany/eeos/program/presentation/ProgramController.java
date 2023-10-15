package com.blackcompany.eeos.program.presentation;

import com.blackcompany.eeos.common.presentation.respnose.ApiResponse;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseBody.SuccessBody;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseGenerator;
import com.blackcompany.eeos.common.presentation.respnose.MessageCode;
import com.blackcompany.eeos.program.application.dto.CreateProgramRequest;
import com.blackcompany.eeos.program.application.usecase.CreateProgramUsecase;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/program")
public class ProgramController {

	private final CreateProgramUsecase createBoardUsecase;

	@PostMapping
	public ApiResponse<SuccessBody<Long>> create(@RequestBody @Valid CreateProgramRequest request) {
		Long programId = createBoardUsecase.create(request);
		return ApiResponseGenerator.success(programId, HttpStatus.CREATED, MessageCode.CREATE);
	}
}
