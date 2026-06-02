package com.blackcompany.eeos.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Gateway가 주입한 X-User-Passport 헤더를 읽어 SecurityContext를 설정하는 필터.
 *
 * <p>Passport roles("USER", "ADMIN")에 "ROLE_" 접두사를 붙여 Spring Security hasAnyRole()과 호환되도록
 * 한다.
 */
@Slf4j
public class PassportAuthenticationFilter extends OncePerRequestFilter {

	static final String PASSPORT_HEADER = "X-User-Passport";
	private static final ObjectMapper mapper =
			new ObjectMapper().registerModule(new JavaTimeModule());

	@Override
	protected void doFilterInternal(
			HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		String passportHeader = request.getHeader(PASSPORT_HEADER);
		if (passportHeader != null && !passportHeader.isBlank()) {
			try {
				byte[] decoded = Base64.getDecoder().decode(passportHeader);
				@SuppressWarnings("unchecked")
				Map<String, Object> claims = mapper.readValue(decoded, Map.class);

				Long memberId = toLong(claims.get("memberId"));
				if (memberId != null) {
					List<String> roles = toStringList(claims.get("roles"));
					JwtAuthentication auth =
							new JwtAuthentication(
									memberId,
									roles.stream()
											.map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
											.map(SimpleGrantedAuthority::new)
											.toList());
					SecurityContextHolder.getContext().setAuthentication(auth);
					log.debug("Passport 인증 설정: memberId={}", memberId);
				}
			} catch (Exception e) {
				log.warn("X-User-Passport 파싱 실패: {}", e.getMessage());
			}
		}
		chain.doFilter(request, response);
	}

	private Long toLong(Object value) {
		if (value instanceof Number n) return n.longValue();
		if (value instanceof String s) {
			try {
				return Long.parseLong(s);
			} catch (NumberFormatException ignored) {
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private List<String> toStringList(Object value) {
		if (value instanceof List<?> list) return (List<String>) list;
		return List.of();
	}
}
