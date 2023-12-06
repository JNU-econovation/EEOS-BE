package com.blackcompany.eeos.program.application.dto.converter;

import com.blackcompany.eeos.program.application.domain.ProgramModel;
import com.blackcompany.eeos.program.application.dto.CommandProgramResponse;
import com.blackcompany.eeos.program.application.dto.QueryProgramResponse;
import com.blackcompany.eeos.program.application.dto.QueryProgramsResponse;
import com.blackcompany.eeos.program.application.model.EventStatus;
import com.blackcompany.eeos.program.persistence.ProgramEntity;
import org.springframework.stereotype.Component;

@Component
public class ProgramResponseConverter {

	public QueryProgramResponse from(ProgramModel target, EventStatus status) {
		return QueryProgramResponse.builder()
				.programId(target.getId())
				.title(target.getTitle())
				.content(target.getContent())
				.programDate(target.getProgramDate())
				.eventStatus(status.name())
				.build();
	}

	public QueryProgramsResponse from(ProgramEntity target) {
		return QueryProgramsResponse.builder()
				.programId(target.getId())
				.title(target.getTitle())
				.content(target.getContent())
				.programDate(target.getProgramDate())
				.build();
	}

	public CommandProgramResponse from(Long id) {
		return CommandProgramResponse.builder().programId(id).build();
	}
}
