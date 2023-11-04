package com.blackcompany.eeos.program.application.dto.suppport;

import com.blackcompany.eeos.common.support.dto.AbstractRequestDto;
import java.sql.Timestamp;

public interface AbstractProgramRequest extends AbstractRequestDto {
	String getTitle();

	String getContent();

	Timestamp getProgramDate();
}
