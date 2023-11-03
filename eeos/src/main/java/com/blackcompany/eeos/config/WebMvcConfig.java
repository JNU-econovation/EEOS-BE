package com.blackcompany.eeos.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
	private final long MAX_AGE_SECS = 3600;
	public static final String ALLOWED_METHOD_NAMES = "GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,PATCH";

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry
				.addMapping("/api/**")
				.allowedOrigins("http://localhost:3000", "https://black-company-upstream.vercel.app/")
				.allowedMethods(ALLOWED_METHOD_NAMES.split(","))
				.exposedHeaders("Authorization")
				.allowCredentials(true)
				.maxAge(MAX_AGE_SECS);
	}
}
