package com.blackcompany.eeos.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	private static final String SECURITY_SCHEME_NAME = "bearerAuth";

	@Bean
	public OpenAPI api() {
		return new OpenAPI()
				.info(info())
				.addSecurityItem(securityRequirement())
				.components(components());
	}

	private Info info() {
		return new Info().title("EEOS API").description("EEOS API Swagger").version("2.2.0");
	}

	private SecurityRequirement securityRequirement() {
		return new SecurityRequirement().addList(SECURITY_SCHEME_NAME);
	}

	private Components components() {
		return new Components().addSecuritySchemes(SECURITY_SCHEME_NAME, securityScheme());
	}

	private SecurityScheme securityScheme() {
		return new SecurityScheme()
				.name("bearerAuth")
				.type(SecurityScheme.Type.HTTP)
				.scheme("bearer")
				.bearerFormat("JWT")
				.in(SecurityScheme.In.HEADER);
	}
}
