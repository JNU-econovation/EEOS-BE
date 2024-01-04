package com.blackcompany.eeos.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {
	private static final long MAX_AGE_SECS = 3600;
	private final List<String> allowOriginUrlPatterns;
	public static final String ALLOWED_METHOD_NAMES = "GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,PATCH";

	public WebMvcConfig(
			@Value("${cors.allow-origin.urls}") final List<String> allowOriginUrlPatterns) {
		this.allowOriginUrlPatterns = allowOriginUrlPatterns;
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		String[] patterns = allowOriginUrlPatterns.toArray(String[]::new);

		registry
				.addMapping("/api/**")
				.allowedOriginPatterns(patterns)
				.allowedMethods(ALLOWED_METHOD_NAMES.split(","))
				.exposedHeaders("Authorization", "Set-Cookie")
				.allowCredentials(true)
				.maxAge(MAX_AGE_SECS);
	}
}
