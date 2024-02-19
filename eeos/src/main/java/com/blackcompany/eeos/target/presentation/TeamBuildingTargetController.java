package com.blackcompany.eeos.target.presentation;

import com.blackcompany.eeos.auth.presentation.support.Member;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponse;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseBody.SuccessBody;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseGenerator;
import com.blackcompany.eeos.common.presentation.respnose.MessageCode;
import com.blackcompany.eeos.target.application.dto.AttendTeamBuildingRequest;
import com.blackcompany.eeos.target.application.usecase.AttendTeamBuildingUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TeamBuildingTargetController {
	private final AttendTeamBuildingUsecase attendTeamBuildingUsecase;

	@PostMapping("/target/team-building")
	public ApiResponse<SuccessBody<Void>> attendTeamBuilding(
			@Member Long memberId, @RequestBody AttendTeamBuildingRequest request) {

		attendTeamBuildingUsecase.create(memberId, request);
		return ApiResponseGenerator.success(HttpStatus.CREATED, MessageCode.CREATE);
	}
}
