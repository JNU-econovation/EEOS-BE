package com.blackcompany.eeos.config;

import com.blackcompany.eeos.common.exception.AsyncExceptionHandler;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {
	private static final int THREAD_COUNT = 4;

	@Override
	public Executor getAsyncExecutor() {
		return Executors.newFixedThreadPool(THREAD_COUNT);
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new AsyncExceptionHandler();
	}
}
