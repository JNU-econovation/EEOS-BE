package com.blackcompany.eeos.program.application.model.converter;

import com.blackcompany.eeos.common.support.converter.AbstractDtoConverter;
import com.blackcompany.eeos.common.utils.DateConverter;
import com.blackcompany.eeos.program.application.dto.CommandProgramRequest;
import com.blackcompany.eeos.program.application.model.ProgramModel;
import com.blackcompany.eeos.program.persistence.ProgramCategory;
import com.blackcompany.eeos.program.persistence.ProgramType;
import org.springframework.stereotype.Component;

@Component
public class ProgramRequestConverter
		implements AbstractDtoConverter<CommandProgramRequest, ProgramModel> {

	@Override
	public ProgramModel from(CommandProgramRequest source) {
		return ProgramModel.builder()
				.title(source.getTitle())
				.content(source.getContent())
				.programDate(DateConverter.toEpochSecond(source.getDeadLine()))
				.programCategory(ProgramCategory.find(source.getCategory()))
				.programType(ProgramType.find(source.getType()))
				.build();
	}

	public ProgramModel from(Long memberId, CommandProgramRequest source) {
		return ProgramModel.builder()
				.title(source.getTitle())
				.content(source.getContent())
				.programDate(DateConverter.toEpochSecond(source.getDeadLine()))
				.programCategory(ProgramCategory.find(source.getCategory()))
				.programType(ProgramType.find(source.getType()))
				.writer(memberId)
				.build();
	}

	public ProgramModel from(Long memberId, CommandProgramRequest source, Long programId) {
		return ProgramModel.builder()
				.id(programId)
				.title(source.getTitle())
				.content(source.getContent())
				.programDate(DateConverter.toEpochSecond(source.getDeadLine()))
				.programCategory(ProgramCategory.find(source.getCategory()))
				.programType(ProgramType.find(source.getType()))
				.writer(memberId)
				.build();
	}
}
