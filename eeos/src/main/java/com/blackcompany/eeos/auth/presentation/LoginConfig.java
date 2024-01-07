package com.blackcompany.eeos.auth.presentation;

import com.blackcompany.eeos.auth.application.domain.token.TokenValidator;
import com.blackcompany.eeos.auth.presentation.interceptor.AuthInterceptor;
import com.blackcompany.eeos.auth.presentation.support.CookieTokenExtractor;
import com.blackcompany.eeos.auth.presentation.support.HeaderTokenExtractor;
import com.blackcompany.eeos.auth.presentation.support.MemberArgumentResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class LoginConfig implements WebMvcConfigurer {
	private final TokenValidator tokenValidator;
	private final MemberArgumentResolver memberArgumentResolver;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry
				.addInterceptor(memberAuthInterceptor())
				.addPathPatterns("/api/**")
				.excludePathPatterns("/api/auth/**", "/api/health-check");
		registry
				.addInterceptor(reissueAuthInterceptor())
				.addPathPatterns("/auth/reissue")
				.excludePathPatterns("/api/auth/**", "/api/health-check");
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(memberArgumentResolver);
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
