package com.blackcompany.eeos.common.utils;

// HTTP Request 를 보낸 사용자의 정보를 저장하는 클래스
public class RequestScope {

	public static final ThreadLocal<Long> requesterInfo = new ThreadLocal<>();

	public static Long getMemberId() {
		return requesterInfo.get();
	}

	public static void setMemberId(Long memberId) {
		requesterInfo.set(memberId);
	}

	public static void clear() {
		requesterInfo.remove();
	}
}
