package com.blackcompany.eeos.member.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotFoundMemberException extends BusinessException {

	public NotFoundMemberException(String message) {
		super(message, HttpStatus.NOT_FOUND);
	}
}
