package com.blackcompany.eeos.common.presentation.support;

public final class AuthorizationScheme {
	public static final String VERIFICATION = "Verification ";
	public static final String BEARER = "Bearer";
	public static final String BEARER_PREFIX = "Bearer ";

	private AuthorizationScheme() {}

	public static String formatBearerToken(String token) {
		return String.format("%s %s", BEARER, token);
	}
}
