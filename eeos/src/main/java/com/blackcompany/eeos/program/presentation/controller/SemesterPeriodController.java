package com.blackcompany.eeos.program.presentation.controller;

import com.blackcompany.eeos.common.presentation.response.ApiResponse;
import com.blackcompany.eeos.common.presentation.response.ApiResponseBody;
import com.blackcompany.eeos.common.presentation.response.ApiResponseGenerator;
import com.blackcompany.eeos.common.presentation.response.MessageCode;
import com.blackcompany.eeos.program.application.dto.request.SemesterPeriodApplicationCommand;
import com.blackcompany.eeos.program.application.dto.response.SemesterPeriodApplicationQuery;
import com.blackcompany.eeos.program.application.usecase.GetSemesterPeriodUsecase;
import com.blackcompany.eeos.program.application.usecase.UpdateSemesterPeriodUsecase;
import com.blackcompany.eeos.program.presentation.dto.UpdateSemesterPeriodRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SemesterPeriodController {
	private final GetSemesterPeriodUsecase getSemesterPeriodUsecase;
	private final UpdateSemesterPeriodUsecase updateSemesterPeriodUsecase;

	@PutMapping("/admin/semester-periods")
	public ApiResponse<ApiResponseBody.SuccessBody<SemesterPeriodApplicationQuery>>
			updateSemesterPeriod(@RequestBody @Valid UpdateSemesterPeriodRequest request) {
		SemesterPeriodApplicationQuery response =
				updateSemesterPeriodUsecase.updateSemesterPeriod(
						new SemesterPeriodApplicationCommand(request.startDate(), request.endDate()));
		return ApiResponseGenerator.success(response, HttpStatus.CREATED, MessageCode.CREATE);
	}

	@GetMapping("/admin/semester-periods")
	public ApiResponse<ApiResponseBody.SuccessBody<SemesterPeriodApplicationQuery>>
			getSemesterPeriod() {
		SemesterPeriodApplicationQuery response = getSemesterPeriodUsecase.getSemesterPeriod();
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.GET);
	}
}
