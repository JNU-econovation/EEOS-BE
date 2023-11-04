package com.blackcompany.eeos.program.application.dto.suppport;

import com.blackcompany.eeos.common.support.dto.AbstractResponseDto;
import java.sql.Timestamp;

public interface AbstractProgramResponse extends AbstractResponseDto {
	Long getId();

	String getTitle();

	String getContent();

	Timestamp getProgramDate();

	String getEventStatus();
}
