package com.blackcompany.eeos.member.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class DeniedMemberEditException extends BusinessException {
	private static final String FAIL_CODE = "3004";
	private final Long memberId;

	public DeniedMemberEditException(Long memberId) {
		super(FAIL_CODE, HttpStatus.FORBIDDEN);
		this.memberId = memberId;
	}

	@Override
	public String getMessage() {
		return String.format("\"%s 은 회원 편집 권한(생성/삭제)이 없는 사용자 입니다.\", memberId");
	}
}
