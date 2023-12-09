package com.blackcompany.eeos.program.application.model.converter;

import com.blackcompany.eeos.common.support.converter.AbstractEntityConverter;
import com.blackcompany.eeos.program.application.model.ProgramModel;
import com.blackcompany.eeos.program.persistence.ProgramEntity;
import org.springframework.stereotype.Component;

@Component
public class ProgramEntityConverter
		implements AbstractEntityConverter<ProgramEntity, ProgramModel> {

	@Override
	public ProgramModel from(ProgramEntity source) {
		return ProgramModel.builder()
				.id(source.getId())
				.title(source.getTitle())
				.programDate(source.getProgramDate())
				.content(source.getContent())
				.build();
	}

	@Override
	public ProgramEntity toEntity(ProgramModel source) {
		return ProgramEntity.builder()
				.id(source.getId())
				.title(source.getTitle())
				.content(source.getContent())
				.programDate(source.getProgramDate())
				.build();
	}
}
