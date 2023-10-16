package com.blackcompany.eeos.program.application.dto.suppport;

import com.blackcompany.eeos.common.support.dto.AbstractResponseDto;

public interface AbstractProgramResponse extends AbstractResponseDto {
	Long getId();

	String getTitle();

	String getContent();

	Long getProgramDate();

	String getEventStatus();
}
