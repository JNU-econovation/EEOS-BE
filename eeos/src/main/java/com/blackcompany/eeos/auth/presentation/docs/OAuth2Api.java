package com.blackcompany.eeos.auth.presentation.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.springframework.http.ResponseEntity;

@Tag(name = "OAuth2 인증", description = "Web/App 분리 인증 API")
public interface OAuth2Api {

	@Operation(
			summary = "인증 진입점",
			description =
					"client_id, redirect_uri를 검증하고 로그인 페이지로 리다이렉트한다. " + "APP 클라이언트는 code_challenge가 필수이다.")
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode = "302",
				description = "로그인 페이지로 리다이렉트"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode = "400",
				description =
						"요청 검증 실패\n\n"
								+ "| 코드 | 메시지 |\n"
								+ "|------|--------|\n"
								+ "| 4010 | 유효하지 않은 클라이언트 |\n"
								+ "| 4011 | 등록되지 않은 redirect URI |",
				content = @Content)
	})
	ResponseEntity<Void> authorize(
			@Parameter(description = "클라이언트 ID", required = true) String clientId,
			@Parameter(description = "리다이렉트 URI", required = true) String redirectUri,
			@Parameter(description = "응답 타입 (code만 지원)", required = true) String responseType,
			@Parameter(description = "CSRF 방지용 상태값", required = true) String state,
			@Parameter(description = "PKCE code_challenge (APP 필수)") String codeChallenge,
			@Parameter(description = "PKCE 방식 (S256만 지원)") String codeChallengeMethod);

	@Operation(
			summary = "OAuth2 로그인",
			description =
					"credentials를 검증하고 clientType에 따라 분기한다. "
							+ "WEB: 쿠키에 AT/RT 설정 후 303 redirect. "
							+ "APP: authorization_code 발급 후 303 redirect.")
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode = "303",
				description = "인증 성공 후 redirect_uri로 리다이렉트"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode = "400",
				description = "client_id 또는 redirect_uri 검증 실패",
				content = @Content),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode = "429",
				description = "4290: 로그인 시도 횟수 초과",
				content = @Content)
	})
	ResponseEntity<Void> login(
			@Parameter(description = "클라이언트 ID", required = true) String clientId,
			@Parameter(description = "리다이렉트 URI", required = true) String redirectUri,
			@Parameter(description = "CSRF 상태값", required = true) String state,
			@Parameter(description = "이메일", required = true) String email,
			@Parameter(description = "비밀번호", required = true) String password,
			@Parameter(description = "PKCE code_challenge") String codeChallenge,
			@Parameter(description = "PKCE 방식") String codeChallengeMethod,
			HttpServletRequest request,
			HttpServletResponse response);

	@Operation(
			summary = "토큰 교환",
			description =
					"authorization_code를 AT/RT로 교환한다. "
							+ "Content-Type: application/x-www-form-urlencoded. "
							+ "PKCE code_verifier 검증 필수.")
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode = "200",
				description = "토큰 발급 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode = "400",
				description =
						"교환 실패\n\n"
								+ "| 코드 | 메시지 |\n"
								+ "|------|--------|\n"
								+ "| 4010 | client_id 불일치 |\n"
								+ "| 4012 | code 만료/사용됨/PKCE 실패/redirect_uri 불일치 |",
				content = @Content)
	})
	ResponseEntity<Map<String, Object>> token(
			@Parameter(description = "grant_type (authorization_code만 지원)", required = true)
					String grantType,
			@Parameter(description = "authorization code", required = true) String code,
			@Parameter(description = "PKCE code_verifier", required = true) String codeVerifier,
			@Parameter(description = "리다이렉트 URI", required = true) String redirectUri,
			@Parameter(description = "클라이언트 ID", required = true) String clientId);
}
