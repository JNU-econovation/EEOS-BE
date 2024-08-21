package com.blackcompany.eeos.auth.presentation.docs;

import com.blackcompany.eeos.auth.application.dto.request.EEOSLoginRequest;
import com.blackcompany.eeos.auth.application.dto.response.TokenResponse;
import com.blackcompany.eeos.auth.presentation.support.Member;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponse;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseBody.SuccessBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
	ApiResponse<SuccessBody<TokenResponse>> login(
			@Parameter(description = "로그인 요청 정보", required = true) @RequestBody EEOSLoginRequest request,
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
