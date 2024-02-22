package com.blackcompany.eeos.teamBuilding.presentation;

import com.blackcompany.eeos.auth.presentation.support.Member;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponse;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseBody.SuccessBody;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseGenerator;
import com.blackcompany.eeos.common.presentation.respnose.MessageCode;
import com.blackcompany.eeos.teamBuilding.application.dto.CreateTeamBuildingRequest;
import com.blackcompany.eeos.teamBuilding.application.dto.ResultTeamBuildingResponse;
import com.blackcompany.eeos.teamBuilding.application.dto.ValidateTeamBuildingResponse;
import com.blackcompany.eeos.teamBuilding.application.usecase.CompleteTeamBuildingUsecase;
import com.blackcompany.eeos.teamBuilding.application.usecase.CreateTeamBuildingUsecase;
import com.blackcompany.eeos.teamBuilding.application.usecase.EndTeamBuildingUsecase;
import com.blackcompany.eeos.teamBuilding.application.usecase.GetResultTeamBuildingUsecase;
import com.blackcompany.eeos.teamBuilding.application.usecase.GetTeamBuildingUsecase;
import com.blackcompany.eeos.teamBuilding.application.usecase.QueryTeamBuildingResponse;
import com.blackcompany.eeos.teamBuilding.application.usecase.ValidateTeamBuildingUsecase;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/team-building")
public class TeamBuildingController {
	private final CreateTeamBuildingUsecase createTeamBuildingUsecase;
	private final EndTeamBuildingUsecase endTeamBuildingUsecase;
	private final ValidateTeamBuildingUsecase validateTeamBuildingUsecase;
	private final CompleteTeamBuildingUsecase completeTeamBuildingUsecase;
	private final GetResultTeamBuildingUsecase getResultTeamBuildingUsecase;
	private final GetTeamBuildingUsecase getTeamBuildingUsecase;

	@PostMapping
	public ApiResponse<SuccessBody<Void>> create(
			@Member Long memberId, @RequestBody @Valid CreateTeamBuildingRequest request) {
		createTeamBuildingUsecase.create(memberId, request);
		return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.CREATE);
	}

	@DeleteMapping("/end")
	public ApiResponse<SuccessBody<Void>> end(@Member Long memberId) {
		endTeamBuildingUsecase.delete(memberId);
		return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.DELETE);
	}

	@GetMapping("/validate")
	public ApiResponse<SuccessBody<ValidateTeamBuildingResponse>> validateStatus(
			@Member Long memberId, @RequestParam("status") String status) {
		ValidateTeamBuildingResponse response = validateTeamBuildingUsecase.validate(memberId, status);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.GET);
	}

	@PostMapping("/complete")
	public ApiResponse<SuccessBody<Void>> complete(@Member Long memberId) {
		completeTeamBuildingUsecase.complete(memberId);
		return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.DELETE);
	}

	@GetMapping("/result")
	public ApiResponse<SuccessBody<ResultTeamBuildingResponse>> getResult(@Member Long memberId) {
		ResultTeamBuildingResponse response = getResultTeamBuildingUsecase.getResult(memberId);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.GET);
	}

	@GetMapping
	public ApiResponse<SuccessBody<QueryTeamBuildingResponse>> getTeamBuilding(
			@Member Long memberId) {
		QueryTeamBuildingResponse response = getTeamBuildingUsecase.getTeamBuilding(memberId);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.GET);
	}
}
