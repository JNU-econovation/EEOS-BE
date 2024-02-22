package com.blackcompany.eeos.teamBuilding.application.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class NotFoundProgressTeamBuildingException extends BusinessException {
	public NotFoundProgressTeamBuildingException(String code, HttpStatus httpStatus) {
		super(code, httpStatus);
	}
}
