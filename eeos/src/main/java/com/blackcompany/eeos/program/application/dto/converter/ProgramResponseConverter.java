package com.blackcompany.eeos.program.application.dto.converter;

import com.blackcompany.eeos.program.application.domain.ProgramModel;
import com.blackcompany.eeos.program.application.dto.CommandProgramResponse;
import com.blackcompany.eeos.program.application.dto.GetProgramResponse;
import com.blackcompany.eeos.program.application.dto.GetProgramsResponse;
import com.blackcompany.eeos.program.application.model.EventStatus;
import com.blackcompany.eeos.program.persistence.ProgramEntity;
import org.springframework.stereotype.Component;

@Component
public class ProgramResponseConverter {

	public GetProgramResponse from(ProgramModel target, EventStatus status) {
		return GetProgramResponse.builder()
				.id(target.getId())
				.title(target.getTitle())
				.content(target.getContent())
				.programDate(target.getProgramDate())
				.eventStatus(status.name())
				.build();
	}

	public GetProgramsResponse from(ProgramEntity target) {
		return GetProgramsResponse.builder()
				.id(target.getId())
				.title(target.getTitle())
				.content(target.getContent())
				.programDate(target.getProgramDate())
				.build();
	}

	public CommandProgramResponse from(Long id) {
		return CommandProgramResponse.builder().programId(id).build();
	}
}
