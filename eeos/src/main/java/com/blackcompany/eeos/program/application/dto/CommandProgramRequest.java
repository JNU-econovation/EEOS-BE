package com.blackcompany.eeos.program.application.dto;

import com.blackcompany.eeos.common.support.dto.AbstractRequestDto;
import java.sql.Timestamp;

public interface CommandProgramRequest extends AbstractRequestDto {

	String getTitle();

	Timestamp getDeadLine();

	String getContent();

	String getCategory();

	String getType();
}
