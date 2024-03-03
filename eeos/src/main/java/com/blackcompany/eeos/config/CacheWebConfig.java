package com.blackcompany.eeos.config;

import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.WebContentInterceptor;

@Configuration
public class CacheWebConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(final InterceptorRegistry registry) {
		CacheControl cacheControl = CacheControl.maxAge(60, TimeUnit.SECONDS);

		WebContentInterceptor interceptor = new WebContentInterceptor();
		interceptor.addCacheMapping(cacheControl, "/api/**");

		registry.addInterceptor(interceptor);
	}
}
