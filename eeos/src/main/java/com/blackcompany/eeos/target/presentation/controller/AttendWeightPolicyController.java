package com.blackcompany.eeos.target.presentation.controller;

import com.blackcompany.eeos.common.presentation.respnose.ApiResponse;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseBody;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseGenerator;
import com.blackcompany.eeos.common.presentation.respnose.MessageCode;
import com.blackcompany.eeos.target.application.dto.AttendWeightPolicyApplicationDto;
import com.blackcompany.eeos.target.application.usecase.CommandAttendWeightPolicyUsecase;
import com.blackcompany.eeos.target.application.usecase.GetAttendWeightPolicyUsecase;
import com.blackcompany.eeos.target.presentation.docs.weightPolicy.presentation.docs.PenaltyApi;
import com.blackcompany.eeos.target.presentation.dto.AttendWeightPolicyWebDto;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/attend/weight-policy") // TODO : 인터셉터단에서 어드민 이외 유저 제한 필요
public class AttendWeightPolicyController implements PenaltyApi {
	private final CommandAttendWeightPolicyUsecase commandWeightPolicyUsecase;
	private final GetAttendWeightPolicyUsecase getWeightPolicyUsecase;

	@PutMapping
	public ApiResponse<ApiResponseBody.SuccessBody<Void>> createWeightPolicy(
			@RequestBody AttendWeightPolicyWebDto request) {
		commandWeightPolicyUsecase.changeWeightPolicy(request.toApplicationRequest());
		return ApiResponseGenerator.success(HttpStatus.CREATED, MessageCode.UPDATE);
	}

	@GetMapping
	public ApiResponse<ApiResponseBody.SuccessBody<AttendWeightPolicyWebDto>> getWeightPolicy(
			@RequestParam Set<String> attendStatuses) {

		AttendWeightPolicyApplicationDto result =
				getWeightPolicyUsecase.getWeightPolicies(attendStatuses);

		AttendWeightPolicyWebDto response =
				AttendWeightPolicyWebDto.builder()
						.policies(
								result.getPolicies().stream()
										.map(
												policy ->
														AttendWeightPolicyWebDto.WeightPolicyDto.builder()
																.signType(policy.getSignType().getType())
																.type(policy.getType().getStatus())
																.score(policy.getScore())
																.build())
										.toList())
						.build();

		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.GET);
	}
}
