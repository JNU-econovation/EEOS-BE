package com.blackcompany.eeos.program.application.model.converter;

import com.blackcompany.eeos.common.support.converter.AbstractDtoConverter;
import com.blackcompany.eeos.common.utils.DateConverter;
import com.blackcompany.eeos.program.application.dto.CreateProgramRequest;
import com.blackcompany.eeos.program.application.dto.UpdateProgramRequest;
import com.blackcompany.eeos.program.application.model.ProgramModel;
import com.blackcompany.eeos.program.persistence.ProgramCategory;
import com.blackcompany.eeos.program.persistence.ProgramType;
import org.springframework.stereotype.Component;

@Component
public class ProgramRequestConverter
		implements AbstractDtoConverter<CreateProgramRequest, ProgramModel> {

	@Override
	public ProgramModel from(CreateProgramRequest source) {
		return ProgramModel.builder()
				.title(source.getTitle())
				.content(source.getContent())
				.programDate(DateConverter.toEpochSecond(source.getDeadLine()))
				.programCategory(ProgramCategory.find(source.getCategory()))
				.programType(ProgramType.find(source.getType()))
				.build();
	}

	public ProgramModel from(Long memberId, CreateProgramRequest source) {
		return ProgramModel.builder()
				.title(source.getTitle())
				.content(source.getContent())
				.programDate(DateConverter.toEpochSecond(source.getDeadLine()))
				.programCategory(ProgramCategory.find(source.getCategory()))
				.programType(ProgramType.find(source.getType()))
				.writer(memberId)
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
