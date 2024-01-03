package com.blackcompany.eeos.auth.presentation.support;

import javax.servlet.http.HttpServletRequest;

public interface TokenExtractor {

	String extract(HttpServletRequest request);
}
