package com.blackcompany.eeos.auth.presentation.docs;

import com.blackcompany.eeos.auth.application.dto.request.EEOSLoginRequest;
import com.blackcompany.eeos.auth.application.dto.response.TokenResponse;
import com.blackcompany.eeos.common.presentation.response.ApiResponse;
import com.blackcompany.eeos.common.presentation.response.ApiResponseBody.SuccessBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "v1 인증", description = "단순 로그인 API (Client-Type 헤더로 Web/App 분기)")
public interface V1AuthApi {

	@Operation(
			summary = "v1 로그인",
			description =
					"id/password로 로그인한다. Client-Type 헤더로 Web/App을 구분한다.\n\n"
							+ "- **WEB**: RT는 쿠키, AT는 body 반환\n"
							+ "- **APP**: AT + RT 모두 body 반환 (쿠키 없음)")
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode = "201",
				description = "로그인 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode = "401",
				description = "| 코드 | 메시지 |\n" + "|------|--------|\n" + "| 4008 | ID 또는 비밀번호가 일치하지 않습니다 |",
				content = @Content)
	})
	ApiResponse<SuccessBody<TokenResponse>> login(
			@Parameter(description = "클라이언트 타입 (WEB 또는 APP, 기본값: WEB)", example = "WEB")
					@RequestHeader(value = "Client-Type", defaultValue = "WEB")
					String clientType,
			@Parameter(description = "로그인 요청 정보", required = true) @RequestBody EEOSLoginRequest request,
			HttpServletResponse httpResponse);
}
