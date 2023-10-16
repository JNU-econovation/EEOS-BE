package com.blackcompany.eeos.program.application.dto.suppport;

import com.blackcompany.eeos.common.support.dto.AbstractRequestDto;

public interface AbstractProgramRequest extends AbstractRequestDto {
	String getTitle();

	String getContent();

	String getProgramDate();
}
