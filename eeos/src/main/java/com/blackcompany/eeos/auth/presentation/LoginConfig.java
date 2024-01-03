package com.blackcompany.eeos.auth.presentation;

import com.blackcompany.eeos.auth.application.domain.token.TokenValidator;
import com.blackcompany.eeos.auth.presentation.interceptor.AuthInterceptor;
import com.blackcompany.eeos.auth.presentation.support.CookieTokenExtractor;
import com.blackcompany.eeos.auth.presentation.support.HeaderTokenExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class LoginConfig implements WebMvcConfigurer {
	private final TokenValidator tokenValidator;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry
				.addInterceptor(memberAuthInterceptor())
				.addPathPatterns("/**")
				.excludePathPatterns("/api/auth/**");
		registry.addInterceptor(reissueAuthInterceptor()).addPathPatterns("/auth/reissue");
	}

	@Bean
	public AuthInterceptor memberAuthInterceptor() {
		return AuthInterceptor.builder()
				.tokenExtractor(new HeaderTokenExtractor())
				.tokenValidator(tokenValidator)
				.build();
	}

	@Bean
	public AuthInterceptor reissueAuthInterceptor() {
		return AuthInterceptor.builder()
				.tokenExtractor(new CookieTokenExtractor())
				.tokenValidator(tokenValidator)
				.build();
	}
}
