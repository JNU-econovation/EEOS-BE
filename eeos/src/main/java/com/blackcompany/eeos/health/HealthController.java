package com.blackcompany.eeos.health;

import com.blackcompany.eeos.common.presentation.respnose.ApiResponse;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseBody.SuccessBody;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseGenerator;
import com.blackcompany.eeos.common.presentation.respnose.MessageCode;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class HealthController {

	@Operation(summary = "헬스 체크를 한다.", description = "서버가 정상적으로 동작하는지 확인한다.")
	@GetMapping("/health-check")
	public ApiResponse<SuccessBody<Void>> checkHealth() {
		return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.GET);
	}
}
