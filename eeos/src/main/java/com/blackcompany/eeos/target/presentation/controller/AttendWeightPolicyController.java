package com.blackcompany.eeos.target.presentation.controller;

import com.blackcompany.eeos.common.presentation.respnose.ApiResponse;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseBody;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseGenerator;
import com.blackcompany.eeos.common.presentation.respnose.MessageCode;
import com.blackcompany.eeos.target.application.dto.AttendWeightPolicyApplicationDto;
import com.blackcompany.eeos.target.application.usecase.CommandAttendWeightPolicyUsecase;
import com.blackcompany.eeos.target.application.usecase.GetAttendWeightPolicyUsecase;
import com.blackcompany.eeos.target.presentation.docs.AttendWeightPolicyApi;
import com.blackcompany.eeos.target.presentation.dto.AttendWeightPolicyWebDto;
import jakarta.validation.Valid;
import java.util.Collections;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/attend/weight-policy") // TODO : 인터셉터단에서 어드민 이외 유저 제한 필요
public class AttendWeightPolicyController implements AttendWeightPolicyApi {
	private final CommandAttendWeightPolicyUsecase commandWeightPolicyUsecase;
	private final GetAttendWeightPolicyUsecase getWeightPolicyUsecase;

	@Override
	@PutMapping
	public ApiResponse<ApiResponseBody.SuccessBody<Void>> createWeightPolicy(
			@RequestBody @Valid AttendWeightPolicyWebDto request) {
		commandWeightPolicyUsecase.changeWeightPolicy(request.toApplicationRequest());
		return ApiResponseGenerator.success(HttpStatus.CREATED, MessageCode.UPDATE);
	}

	@Override
	@GetMapping
	public ApiResponse<ApiResponseBody.SuccessBody<AttendWeightPolicyWebDto>> getWeightPolicy(
			@RequestParam(required = false) Set<String> attendStatuses) {
		Set<String> statuses = attendStatuses != null ? attendStatuses : Collections.emptySet();

		AttendWeightPolicyApplicationDto result = getWeightPolicyUsecase.getWeightPolicies(statuses);

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
