package com.blackcompany.eeos.program.application.dto.converter;

import com.blackcompany.eeos.program.application.domain.ProgramModel;
import com.blackcompany.eeos.program.application.dto.GetProgramResponse;
import org.springframework.stereotype.Component;

@Component
public class ProgramResponseConverter {
	public GetProgramResponse from(ProgramModel target) {
		return GetProgramResponse.builder()
				.id(target.getId())
				.title(target.getTitle())
				.content(target.getContent())
				.programDate(target.getProgramDate())
				.eventStatus(target.getEventStatus())
				.build();
	}
}
