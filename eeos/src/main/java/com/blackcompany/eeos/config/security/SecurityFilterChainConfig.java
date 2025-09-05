package com.blackcompany.eeos.config.security;

import com.blackcompany.eeos.auth.application.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.session.DisableEncodeUrlFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityFilterChainConfig {

	private static final String ADMIN = Role.ROLE_ADMIN.getRole();
	private final AccessTokenFilter authFilter;
	private final OptionsFilter optionsFilter;
	private final DynamicCorsConfigurationSource corsConfigurationSource;
	private final AccessTokenEntryPoint accessTokenEntryPoint;
	private final UnknownEndpointFilter unknownEndpointFilter;

	@Bean
	@Order(0)
	// swagger
	SecurityFilterChain swagger(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.securityMatchers(
				(matcher) -> {
					matcher.requestMatchers("/api/docs/**", "/api/swagger-ui/**", "/api/docs.html");
				});

		commonConfiguration(httpSecurity);

		httpSecurity.logout(AbstractHttpConfigurer::disable);
		httpSecurity.securityContext(AbstractHttpConfigurer::disable);

		return httpSecurity.build();
	}

	@Bean
	@Order(1)
	// 인증 필요 없는 엔드포인트
	SecurityFilterChain nonAuthenticated(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.securityMatchers(
				(matcher) -> {
					matcher
							.requestMatchers(HttpMethod.OPTIONS, "/**") // CORS Preflight 매칭
							.requestMatchers("/api/auth/logout")
							.requestMatchers("/api/auth/login/additional-info")
							.requestMatchers(HttpMethod.POST, "/api/auth/login/**")
							.requestMatchers(HttpMethod.POST, "/api/auth/login")
							.requestMatchers("/api/guest/**")
							.requestMatchers("/api/health-check");
				});

		commonConfiguration(httpSecurity);

		httpSecurity.logout(AbstractHttpConfigurer::disable);
		httpSecurity.securityContext(AbstractHttpConfigurer::disable);

		httpSecurity.cors(
				httpSecurityCorsConfigurer ->
						httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource));

		httpSecurity.addFilterAfter(optionsFilter, CorsFilter.class);

		return httpSecurity.build();
	}

	@Bean
	@Order(2)
	// 인증이 필요한 엔드포인트
	SecurityFilterChain authenticated(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.securityMatchers(
				(matcher) -> {
					matcher
							.requestMatchers("/api/comments/**")
							.requestMatchers("/api/attend/**")
							.requestMatchers("/api/target/**")
							.requestMatchers("/api/members/**")
							.requestMatchers("/api/programs/**")
							.requestMatchers("/api/teams/**")
							.requestMatchers("/api/admin/**")
							.requestMatchers("/api/team-building/**")
							.requestMatchers("/api/semester-periods/**")
							.requestMatchers("/api/calendar/**");
				});

		httpSecurity.authorizeHttpRequests(
				(requests) -> {
					requests.requestMatchers("/api/admin/**").hasAnyRole(ADMIN);
					requests.requestMatchers(HttpMethod.POST, "/api/programs").hasAnyRole(ADMIN);
					requests.requestMatchers(HttpMethod.PATCH, "/api/programs").hasAnyRole(ADMIN);
					requests.requestMatchers(HttpMethod.DELETE, "/api/programs").hasAnyRole(ADMIN);
					requests
							.requestMatchers(HttpMethod.POST, "/api/programs/{programId}/slack/notification")
							.hasAnyRole(ADMIN);
					requests.requestMatchers(HttpMethod.POST, "/api/teams").hasAnyRole(ADMIN);
					requests.requestMatchers(HttpMethod.DELETE, "/api/teams/{teamId}").hasAnyRole(ADMIN);
					requests.requestMatchers(HttpMethod.DELETE, "/api/members/{memberId}").hasAnyRole(ADMIN);
					requests
							.requestMatchers(HttpMethod.PUT, "/api/members/activeStatus/{memberId}")
							.hasAnyRole(ADMIN);
					requests
							.requestMatchers(HttpMethod.PUT, "/api/members/{memberId}/department")
							.hasAnyRole(ADMIN);
					requests.anyRequest().authenticated();
				});

		commonConfiguration(httpSecurity);

		httpSecurity.cors(
				httpSecurityCorsConfigurer ->
						httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource));

		httpSecurity.addFilterAt(authFilter, LogoutFilter.class);
		httpSecurity.exceptionHandling(ex -> ex.authenticationEntryPoint(accessTokenEntryPoint));
		httpSecurity.addFilterAfter(optionsFilter, CorsFilter.class);

		return httpSecurity.build();
	}

	@Bean
	@Order(3)
	SecurityFilterChain unknownEndpoint(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.securityMatcher("/**");

		commonConfiguration(httpSecurity);
		httpSecurity.logout(AbstractHttpConfigurer::disable);
		// 서버가 처리할 수 있는 엔드포인트인지 확인하는 필터
		httpSecurity.addFilterBefore(unknownEndpointFilter, DisableEncodeUrlFilter.class);

		return httpSecurity.build();
	}

	private void commonConfiguration(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.formLogin(AbstractHttpConfigurer::disable);
		httpSecurity.httpBasic(AbstractHttpConfigurer::disable);
		httpSecurity.csrf(AbstractHttpConfigurer::disable);
		httpSecurity.sessionManagement(AbstractHttpConfigurer::disable);
	}
}
