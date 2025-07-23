package com.blackcompany.eeos.common.utils;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

// HTTP Request 를 보낸 사용자의 정보를 저장하는 클래스
@Component
@RequiredArgsConstructor
public class RequestScope {

	public static Long getMemberId() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (Objects.nonNull(authentication)) {
			return (Long) authentication.getPrincipal();
		}

		return null;
	}
}
