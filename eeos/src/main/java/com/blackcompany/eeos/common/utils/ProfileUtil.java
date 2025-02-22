package com.blackcompany.eeos.common.utils;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ProfileUtil {
	private final Set<String> activeProfiles;

	public ProfileUtil(Environment environment) {
		this.activeProfiles =
				Arrays.stream(environment.getActiveProfiles()).collect(Collectors.toSet());
	}

	public String getProfile() {
		if (isLocal()) {
			return "local";
		} else if (isDev()) {
			return "dev";
		} else if (isProd()) {
			return "live";
		}
		return activeProfiles.stream().findFirst().orElse("unknown");
	}

	private boolean isLocal() {
		return activeProfiles.contains("local");
	}

	private boolean isDev() {
		return activeProfiles.contains("dev");
	}

	private boolean isProd() {
		return activeProfiles.contains("prod");
	}
}
