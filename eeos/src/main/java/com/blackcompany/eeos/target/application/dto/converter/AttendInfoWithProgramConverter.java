package com.blackcompany.eeos.target.application.dto.converter;

import com.blackcompany.eeos.program.application.model.ProgramModel;
import com.blackcompany.eeos.target.application.dto.AttendInfoWithProgramResponse;
import com.blackcompany.eeos.target.application.model.AttendModel;
import org.springframework.stereotype.Component;

@Component
public class AttendInfoWithProgramConverter {

	public AttendInfoWithProgramResponse from(AttendModel attendModel, ProgramModel programModel) {
		return AttendInfoWithProgramResponse.builder()
				.attendStatus(attendModel.getStatus())
				.programId(programModel.getId())
				.programStatus(programModel.getProgramStatus())
				.title(programModel.getTitle())
				.build();
	}
}
