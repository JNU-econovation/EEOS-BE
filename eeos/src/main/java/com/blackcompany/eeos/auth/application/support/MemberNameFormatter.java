package com.blackcompany.eeos.auth.application.support;

public class MemberNameFormatter {
	public static String format(String name, Integer generation) {
		return generation + "기 " + name;
	}
}
