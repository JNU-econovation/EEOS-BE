package com.blackcompany.eeos.team.presetation.controller;

import com.blackcompany.eeos.auth.presentation.support.Member;
import com.blackcompany.eeos.common.presentation.response.ApiResponse;
import com.blackcompany.eeos.common.presentation.response.ApiResponseBody.SuccessBody;
import com.blackcompany.eeos.common.presentation.response.ApiResponseGenerator;
import com.blackcompany.eeos.common.presentation.response.MessageCode;
import com.blackcompany.eeos.team.application.dto.CreateTeamRequest;
import com.blackcompany.eeos.team.application.dto.CreateTeamResponse;
import com.blackcompany.eeos.team.application.dto.QueryTeamsResponse;
import com.blackcompany.eeos.team.application.usecase.CreateTeamUsecase;
import com.blackcompany.eeos.team.application.usecase.DeleteTeamUsecase;
import com.blackcompany.eeos.team.application.usecase.GetTeamsByActiveStatus;
import com.blackcompany.eeos.team.presetation.docs.TeamApi;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teams")
public class TeamController implements TeamApi {

	private final CreateTeamUsecase createTeamUsecase;
	private final DeleteTeamUsecase deleteTeamUsecase;
	private final GetTeamsByActiveStatus getTeamsByActiveStatus;

	@Override
	@PostMapping
	public ApiResponse<SuccessBody<CreateTeamResponse>> create(
			@Member Long memberId, @RequestBody @Valid CreateTeamRequest request) {
		CreateTeamResponse response = createTeamUsecase.create(memberId, request);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.CREATE);
	}

	@Override
	@DeleteMapping("/{teamId}")
	public ApiResponse<SuccessBody<Void>> delete(
			@Member Long memberId, @PathVariable("teamId") Long teamId) {
		deleteTeamUsecase.delete(memberId, teamId);
		return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.DELETE);
	}

	@Override
	@GetMapping
	public ApiResponse<SuccessBody<QueryTeamsResponse>> findTeamsByActiveStatus(
			@RequestParam("programId") String programId) {
		QueryTeamsResponse response = getTeamsByActiveStatus.execute(programId);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.GET);
	}
}
