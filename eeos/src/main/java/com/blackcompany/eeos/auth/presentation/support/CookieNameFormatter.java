package com.blackcompany.eeos.auth.presentation.support;

import com.blackcompany.eeos.common.utils.ProfileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CookieNameFormatter {
	private final ProfileUtil profile;

	public String format(String key) {
		return String.format("%s_%s", profile.getProfile(), key);
	}
}
