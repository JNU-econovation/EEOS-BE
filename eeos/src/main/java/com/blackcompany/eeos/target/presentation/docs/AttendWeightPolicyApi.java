package com.blackcompany.eeos.target.presentation.docs;

import com.blackcompany.eeos.common.presentation.response.ApiResponse;
import com.blackcompany.eeos.common.presentation.response.ApiResponseBody;
import com.blackcompany.eeos.target.presentation.dto.AttendWeightPolicyWebDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Set;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "참석 가중치", description = "참석 가중치에 관한 API")
public interface AttendWeightPolicyApi {
	@Operation(summary = "참석 가중치 생성/수정", description = "참석 가중치를 생성하거나 수정한다.")
	ApiResponse<ApiResponseBody.SuccessBody<Void>> createWeightPolicy(
			@RequestBody @Valid AttendWeightPolicyWebDto request);

	@Operation(summary = "참석 가중치 조회", description = "주어진 참석 상태에 대한 참석 가중치를 조회한다.")
	ApiResponse<ApiResponseBody.SuccessBody<AttendWeightPolicyWebDto>> getWeightPolicy(
			@RequestParam(required = false) Set<String> attendStatuses);
}
