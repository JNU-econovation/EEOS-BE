package com.blackcompany.eeos.auth.presentation.docs;

import com.blackcompany.eeos.auth.application.dto.request.EEOSLoginRequest;
import com.blackcompany.eeos.auth.application.dto.response.TokenResponse;
import com.blackcompany.eeos.auth.presentation.dto.EeosSignUpRequest;
import com.blackcompany.eeos.auth.presentation.support.Member;
import com.blackcompany.eeos.common.presentation.response.ApiResponse;
import com.blackcompany.eeos.common.presentation.response.ApiResponseBody.SuccessBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "인증", description = "인증 관련 API")
public interface AuthApi {
	@Operation(
			summary = "OAuth 로그인",
			description = "PathVariable에 담긴 redirect_url, code를 받아 액세스 토큰과 리프레시 토큰을 발급한다.")
	ApiResponse<SuccessBody<TokenResponse>> login(
			@Parameter(description = "OAuth 서버 타입 (예: slack)", required = true) @PathVariable
					String oauthServerType,
			@Parameter(description = "OAuth 인증 코드", required = true) @RequestParam("code") String code,
			@Parameter(description = "리다이렉트 URI", required = true) @RequestParam("redirect_uri")
					String uri,
			HttpServletResponse httpResponse);

	@Operation(summary = "일반 로그인", description = "사용자가 id와 password를 이용하여 로그인한다.")
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode = "200",
				description = "로그인 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode = "401",
				description = "4008: ID 또는 비밀번호가 일치하지 않습니다",
				content = @Content)
	})
	ApiResponse<SuccessBody<TokenResponse>> login(
			@Parameter(description = "로그인 요청 정보", required = true) @RequestBody EEOSLoginRequest request,
			HttpServletResponse httpResponse);

	@Operation(summary = "회원가입", description = "id, password, 기수, 성함, 활동상태로 회원가입하고 토큰을 반환한다.")
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode = "201",
				description = "회원가입 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode = "400",
				description =
						"Validation 에러\n\n"
								+ "| 코드 | 메시지 |\n"
								+ "|------|--------|\n"
								+ "| 4100 | 아이디는 필수 입력값입니다 |\n"
								+ "| 4101 | 아이디는 50자 이하여야 합니다 |\n"
								+ "| 4102 | 비밀번호는 필수 입력값입니다 |\n"
								+ "| 4103 | 비밀번호는 8~20자이며, 영문과 숫자를 포함해야 합니다 |\n"
								+ "| 4104 | 기수는 필수 입력값입니다 |\n"
								+ "| 4105 | 기수는 1 이상이어야 합니다 |\n"
								+ "| 4106 | 성함은 필수 입력값입니다 |\n"
								+ "| 4107 | 성함은 50자 이하여야 합니다 |\n"
								+ "| 4108 | 활동 상태는 필수 입력값입니다 |\n"
								+ "| 4109 | 활동 상태는 am, cm, rm, ob 중 하나여야 합니다 |\n"
								+ "| 3001 | {status}는 존재하지 않는 활동 상태입니다 |",
				content = @Content),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode = "409",
				description = "4009: 이미 사용 중인 아이디입니다",
				content = @Content)
	})
	ApiResponse<SuccessBody<TokenResponse>> signUp(
			@Parameter(description = "회원가입 요청 정보", required = true) @Valid @RequestBody
					EeosSignUpRequest request,
			HttpServletResponse httpResponse);

	@Operation(
			summary = "토큰 재발급",
			description = "쿠키에 담긴 사용자 토큰을 이용하여 리프레시 토큰을 반환한다.",
			security = @SecurityRequirement(name = "bearerAuth"))
	ApiResponse<SuccessBody<TokenResponse>> reissue(
			HttpServletRequest request, HttpServletResponse httpResponse);

	@Operation(
			summary = "로그아웃",
			description = "쿠키에 담긴 리프레시 토큰을 이용하여 로그아웃한다.",
			security = @SecurityRequirement(name = "bearerAuth"))
	ApiResponse<SuccessBody<Void>> logout(
			HttpServletRequest request,
			HttpServletResponse httpResponse,
			@Parameter(hidden = true) @Member Long memberId);

	@Operation(
			summary = "회원탈퇴",
			description = "쿠키에 담긴 리프레시 토큰을 이용하여 회원을 탈퇴한다.",
			security = @SecurityRequirement(name = "bearerAuth"))
	ApiResponse<SuccessBody<Void>> withDraw(
			HttpServletRequest request,
			HttpServletResponse httpResponse,
			@Parameter(hidden = true) @Member Long memberId);
}
