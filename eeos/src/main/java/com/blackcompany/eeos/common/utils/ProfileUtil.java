package com.blackcompany.eeos.common.utils;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProfileUtil {
	private final Environment environment;
	private final Set<String> activeProfiles = new CopyOnWriteArraySet<>();

	private Set<String> getActiveProfiles() {
		if (activeProfiles.isEmpty()) {
			activeProfiles.addAll(
					Arrays.stream(environment.getActiveProfiles()).collect(Collectors.toSet()));
		}
		return activeProfiles;
	}

	public String getProfile() {
		if (isLocal()) {
			return "local";
		} else if (isDev()) {
			return "dev";
		} else if (isLive()) {
			return "live";
		}
		return getFirstProfile();
	}

	public boolean isLocal() {
		return getActiveProfiles().contains("local");
	}

	public boolean isDev() {
		return getActiveProfiles().contains("dev");
	}

	public boolean isLive() {
		return getActiveProfiles().contains("live");
	}

	private String getFirstProfile() {
		return Arrays.stream(environment.getActiveProfiles()).findFirst().orElse("unknown");
	}
}
