package com.blackcompany.eeos.auth.presentation;

import com.blackcompany.eeos.auth.presentation.support.MemberArgumentResolver;
import com.blackcompany.eeos.auth.presentation.support.VerificationAuthorizationResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class LoginConfig implements WebMvcConfigurer {
	private final MemberArgumentResolver memberArgumentResolver;
	private final VerificationAuthorizationResolver verificationAuthorizationResolver;

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.addAll(List.of(memberArgumentResolver, verificationAuthorizationResolver));
	}


}
