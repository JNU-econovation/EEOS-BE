package com.blackcompany.eeos.auth.presentation.docs;

import com.blackcompany.eeos.auth.application.dto.request.ClientRegistrationRequest;
import com.blackcompany.eeos.auth.application.dto.response.ClientRegistrationResponse;
import com.blackcompany.eeos.auth.presentation.support.Member;
import com.blackcompany.eeos.common.presentation.response.ApiResponse;
import com.blackcompany.eeos.common.presentation.response.ApiResponseBody.SuccessBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "클라이언트 관리", description = "OAuth2 클라이언트 등록 API")
public interface ClientApi {

	@Operation(
			summary = "클라이언트 등록",
			description = "OAuth2 클라이언트를 등록한다. WEB은 client_secret이 발급되고, APP은 발급되지 않는다. 관리자만 호출 가능.",
			security = @SecurityRequirement(name = "bearerAuth"))
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode = "201",
				description = "클라이언트 등록 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode = "400",
				description =
						"요청 검증 실패\n\n"
								+ "| 코드 | 메시지 |\n"
								+ "|------|--------|\n"
								+ "| 4011 | redirectUris가 비어있거나 10개 초과 또는 512자 초과 |",
				content = @Content),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode = "403",
				description = "관리자 권한 필요",
				content = @Content)
	})
	ApiResponse<SuccessBody<ClientRegistrationResponse>> register(
			@Parameter(description = "클라이언트 등록 요청 정보", required = true) @RequestBody
					ClientRegistrationRequest request,
			@Parameter(hidden = true) @Member Long memberId);
}
