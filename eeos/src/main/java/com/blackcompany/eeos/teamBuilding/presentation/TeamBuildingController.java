package com.blackcompany.eeos.teamBuilding.presentation;

import com.blackcompany.eeos.auth.presentation.support.Member;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponse;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseBody.SuccessBody;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseGenerator;
import com.blackcompany.eeos.common.presentation.respnose.MessageCode;
import com.blackcompany.eeos.teamBuilding.application.dto.CreateTeamBuildingRequest;
import com.blackcompany.eeos.teamBuilding.application.usecase.CreateTeamBuildingUsecase;
import com.blackcompany.eeos.teamBuilding.application.usecase.EndTeamBuildingUsecase;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TeamBuildingController {
	private final CreateTeamBuildingUsecase createTeamBuildingUsecase;
	private final EndTeamBuildingUsecase endTeamBuildingUsecase;

	@PostMapping("/team-building")
	public ApiResponse<SuccessBody<Void>> create(
			@Member Long memberId, @RequestBody @Valid CreateTeamBuildingRequest request) {
		createTeamBuildingUsecase.create(memberId, request);
		return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.CREATE);
	}

	@DeleteMapping("/team-building/end")
	public ApiResponse<SuccessBody<Void>> end(@Member Long memberId) {
		endTeamBuildingUsecase.delete(memberId);
		return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.DELETE);
	}
}
