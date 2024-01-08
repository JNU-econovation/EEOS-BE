package com.blackcompany.eeos.auth.application.support;

import java.util.regex.Pattern;

public class ValidatorNameFormat {
	private static final String REGULAR_EXPRESSION = "^\\d{2}기.*";
	private static final Pattern FORMAT = Pattern.compile(REGULAR_EXPRESSION);

	public static boolean isSatisfy(String source) {
		return FORMAT.matcher(source).matches();
	}
}
