package com.blackcompany.eeos.program.application.model.converter;

import com.blackcompany.eeos.common.support.converter.AbstractDtoConverter;
import com.blackcompany.eeos.common.utils.DateConverter;
import com.blackcompany.eeos.program.application.dto.CreateProgramRequest;
import com.blackcompany.eeos.program.application.dto.UpdateProgramRequest;
import com.blackcompany.eeos.program.application.model.ProgramModel;
import org.springframework.stereotype.Component;

@Component
public class ProgramRequestConverter
		implements AbstractDtoConverter<CreateProgramRequest, ProgramModel> {

	@Override
	public ProgramModel from(CreateProgramRequest source) {
		return ProgramModel.builder()
				.title(source.getTitle())
				.content(source.getContent())
				.programDate(DateConverter.toEpochSecond(source.getProgramDate()))
				.build();
	}

	public ProgramModel from(Long programId, UpdateProgramRequest source) {
		return ProgramModel.builder()
				.id(programId)
				.title(source.getTitle())
				.content(source.getContent())
				.programDate(source.getProgramDate())
				.build();
	}
}
