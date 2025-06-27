package com.blackcompany.eeos.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

// JwtAuthentication이 없을경우 실행됨 (유효한 access token이 없음)
@Component
public class AccessTokenEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(
			HttpServletRequest request,
			HttpServletResponse response,
			AuthenticationException authException)
			throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
		response.getWriter().write("{\"message\": \"인증이 필요합니다\", \"error_code\": \"2000\"}");
		response.getWriter().flush();
	}
}
