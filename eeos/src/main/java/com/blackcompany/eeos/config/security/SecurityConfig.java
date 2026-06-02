package com.blackcompany.eeos.config.security;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

	@Bean
	public FilterRegistrationBean<OptionsFilter> optionsFilterRegistrationBean(
			OptionsFilter optionsFilter) {
		FilterRegistrationBean<OptionsFilter> registrationBean =
				new FilterRegistrationBean<>(optionsFilter);
		registrationBean.setEnabled(false);
		return registrationBean;
	}

	@Bean
	public FilterRegistrationBean<UnknownEndpointFilter> unknownEndpointFilterRegistrationBean(
			UnknownEndpointFilter unknownEndpointFilter) {
		FilterRegistrationBean<UnknownEndpointFilter> registrationBean =
				new FilterRegistrationBean<>(unknownEndpointFilter);
		registrationBean.setEnabled(false);
		return registrationBean;
	}

	@Bean
	public FilterRegistrationBean<InternalApiKeyFilter> internalApiKeyFilterRegistration(
			InternalApiKeyFilter internalApiKeyFilter) {
		FilterRegistrationBean<InternalApiKeyFilter> registration =
				new FilterRegistrationBean<>(internalApiKeyFilter);
		registration.setEnabled(false);
		return registration;
	}
}
