package com.blackcompany.eeos.program.application.dto.converter;

import com.blackcompany.eeos.program.application.dto.CommandProgramResponse;
import com.blackcompany.eeos.program.application.dto.QueryProgramResponse;
import com.blackcompany.eeos.program.application.dto.QueryProgramsResponse;
import com.blackcompany.eeos.program.application.model.ProgramModel;
import com.blackcompany.eeos.program.application.model.ProgramStatus;
import org.springframework.stereotype.Component;

@Component
public class ProgramResponseConverter {

	public QueryProgramsResponse from(ProgramModel target, ProgramStatus status) {
		return QueryProgramsResponse.builder()
				.programId(target.getId())
				.title(target.getTitle())
				.deadLine(target.getProgramDate())
				.attendMode(target.getAttendMode().getMode())
				.category(target.getProgramCategory().getCategory())
				.type(target.getProgramType().getType())
				.programStatus(status.getStatus())
				.build();
	}

	public QueryProgramResponse from(ProgramModel target, String status, String accessRight) {
		return QueryProgramResponse.builder()
				.programId(target.getId())
				.title(target.getTitle())
				.content(target.getContent())
				.deadLine(target.getProgramDate())
				.category(target.getProgramCategory().getCategory())
				.programGithubUrl(target.getGithubUrl())
				.type(target.getProgramType().getType())
				.attendMode(target.getAttendMode().getMode())
				.programStatus(status)
				.accessRight(accessRight)
				.build();
	}

	public CommandProgramResponse from(Long id) {
		return CommandProgramResponse.builder().programId(id).build();
	}
}
