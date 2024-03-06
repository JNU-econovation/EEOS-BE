package com.blackcompany.eeos.common.presentation.support;

import org.springframework.http.ResponseCookie;

public interface CookieGenerator {
	ResponseCookie createCookie(String key, String value);
}
