package com.blackcompany.eeos.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class InternalApiKeyFilter extends OncePerRequestFilter {

	private static final String API_KEY_HEADER = "X-EEOS-API-KEY";

	@Value("${eeos.internal-api-key}")
	private String internalApiKey;

	@Override
	protected boolean shouldNotFilter(jakarta.servlet.http.HttpServletRequest request) {
		// SecurityConfig 없이 직접 서블릿 필터로 등록될 때 /api/internal/** 이외 경로는 스킵
		return !request.getRequestURI().startsWith("/api/internal/");
	}

	@Override
	protected void doFilterInternal(
			HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		String key = request.getHeader(API_KEY_HEADER);
		if (key == null || !key.equals(internalApiKey)) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		chain.doFilter(request, response);
	}
}
