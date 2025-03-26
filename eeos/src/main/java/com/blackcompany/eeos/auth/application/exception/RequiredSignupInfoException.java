package com.blackcompany.eeos.auth.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import java.util.UUID;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RequiredSignupInfoException extends BusinessException {
	private static final String FAIL_CODE = "4010";
	private final UUID verificationId;

	public RequiredSignupInfoException(UUID verificationId) {
		super(FAIL_CODE, HttpStatus.ACCEPTED);
		this.verificationId = verificationId;
	}

	@Override
	public String getMessage() {
		return "회원가입 시 추가 정보가 필요합니다.";
	}
}
