package com.blackcompany.eeos.common.presentation.support;

import org.springframework.http.ResponseCookie;

public interface CookieManager {
	ResponseCookie setCookie(String key, String value);

	ResponseCookie deleteCookie(String key);
}
